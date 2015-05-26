#include "rssi_list.h"

/*
 * Functions implementation
 */

// General functions
/*!
 * \brief Function string_to_mac converts a human-readable MAC to its binary counterpart.
 * \return the 6-bytes binary MAC
 * \param buf the buffer containing the MAC string
 * \param byte_mac a 6-byte buffer to store the result. byte_mac is returned by the function.
 */
u_char *string_to_mac( char * buf, u_char * byte_mac ) {
	int i = 0, j = 0;
	for ( i = 0; i < 6; i++ ) {

		byte_mac[i] = 0;
		for ( j = i * 3; j <= i * 3 + 1; j++) {

			if ( buf[j] >= '0' && buf[j] <= '9' ) {
				byte_mac[i] += buf[j] - 48;
			}
			else {
				byte_mac[i] += buf[j] - 65;
			}

		}

	}

	return byte_mac;
}

/*!
 * \brief mac_to_string opposite function to string_to_mac.
 * Takes a binary MAC address (such as extracted from IEEE802.11 header by libpcap)
 * and converts it to a human-readable string.
 * \return the string MAC
 * \param byte_mac the binary MAC
 * \param buf an already allocated char buffer, returned by the function.
 */
char * mac_to_string( u_char * byte_mac, char * buf ) {
	sprintf( buf, "%02X:%02X:%02X:%02X:%02X:%02X", byte_mac[0], byte_mac[1], byte_mac[2], byte_mac[3], byte_mac[4], byte_mac[5] );
	return buf;
}

// Rssi_sample functions
/*!
 * \brief clear_outdated_values removes all outdated RSSI in the histogram.
 * \param histogram the list from which to filter the outdated values.
 */
void clear_outdated_values( Rssi_histogram * histogram ) {

	unsigned long long now = get_current_time_ms();
	Rssi_histogram_bar *curr_bar = histogram->bar, *temp_bar;
	Occurence *curr_occurence, *temp_occurence;

	while ( curr_bar != NULL ) {

		curr_occurence = curr_bar->occurence;

		while ( curr_occurence != NULL ) {

			if ( curr_occurence->deadline > now ) {
				break;
			}

			temp_occurence = curr_occurence;
			curr_occurence = curr_occurence->next;
			temp_occurence->next = NULL;
			free( temp_occurence );

		}

		curr_bar->occurence = curr_occurence;
		temp_bar = curr_bar->next;

		if ( curr_occurence == NULL ) {
			remove_bar( histogram, curr_bar );
		}

		curr_bar = temp_bar;

	}

}

/*!
 * \brief add_value adds a new RSSI value in the histogram.
 * \param histogram the histogram to append the RSSI value to.
 * \param value the RSSI value
 */
void add_value( Rssi_histogram * histogram, int value ) {

	Occurence *new_value = malloc( sizeof( Occurence ) ), *temp_occurence = NULL;
	double rssi_mW = pow( 10, (double) value / (double) 10 );
	Rssi_histogram_bar *curr_bar = histogram->bar, *temp_bar = curr_bar;

	new_value->deadline = get_current_time_ms() + DEFAULT_KEEP_DELAY;
	new_value->next = NULL;

	if ( curr_bar == NULL ) {

		histogram->bar = malloc( sizeof( Rssi_histogram_bar ) );
		histogram->bar->rssi_mW = rssi_mW;
		histogram->bar->next = NULL;
		histogram->bar->occurence = new_value;
		return;

	}

	while ( curr_bar != NULL && curr_bar->rssi_mW < rssi_mW ) {
		temp_bar = curr_bar;
		curr_bar = curr_bar->next;
	}

	if ( curr_bar != NULL && curr_bar->rssi_mW == rssi_mW ) {

		temp_occurence = curr_bar->occurence;

		while( temp_occurence->next != NULL ) {
			temp_occurence = temp_occurence->next;
		}

		temp_occurence->next = new_value;

	} else {

		temp_bar->next = malloc( sizeof( Rssi_histogram_bar ) );
		temp_bar->next->rssi_mW = rssi_mW;
		temp_bar->next->occurence = new_value;
		temp_bar->next->next = curr_bar;

	}

}

// Device functions
/*!
 * \brief clear_list fully clears the devices list
 * When the list is cleared, its value shall be NULL.
 * \param list pointer to the list head pointer.
 */
void clear_list( Device ** list ) {

	Device *current = NULL, *next = NULL;

	if ( list == NULL ) {
		return;
	}

	current = *list;

	while ( current != NULL ) {

		next = current->next;
		delete_Histogram( current->histogram.bar );
		free( current );
		current = next;

	}

	free( list );
	list = NULL;

}

/*!
 * \brief find_mac looks up for a MAC address in the list.
 * \return a pointer to the corresponding Device, NULL if not found.
 * \param list the list head pointer.
 * \param mac_value the binary MAC to search.
 */
Device * find_mac( Device * list, u_char * mac_value ) {

	Device *current = list;
	char temp[18];
	char mac[18];

	mac_to_string( mac_value, mac );

	while ( current != NULL &&
		strcmp( mac, mac_to_string( current->mac_addr, temp ) ) != 0 ) {
			current = current->next;
	}

	return current;

}

/*!
 * \brief add_Device adds a Device to the list.
 * \return the list head pointer.
 * \param list a pointer to the list head pointer.
 * \param mac_value the binary MAC of the new node to be added.
 */
Device * add_Device( Device ** list, u_char * mac_value ) {

	Device *current = NULL;
	Device *new_Device = malloc( sizeof( Device ) );
	int i = 0;

	for ( i = 0; i < 6 ; i++ ) {
		new_Device->mac_addr[i] = mac_value[i];
	}

	new_Device->next = NULL;
	new_Device->histogram.bar = NULL;

	current = *list;

	if ( current == NULL ) {
		*list = new_Device;
		return new_Device;
	}

	while ( current->next != NULL ) {
		current = current->next;
	}

	current->next = new_Device;

	return new_Device;

}

/*!
 * \brief delete_Device deletes a Device from the list.
 * \param list a pointer to the list head pointer.
 * \param e a pointer to the Device to be deleted.
 */
void delete_Device( Device ** list, Device * e ) {

	Device *temp = (*list)->next, *prev = *list;

	//The element to remove is the head of the list
	if ( *list == e ) {

		*list = e->next;
		delete_Histogram( e->histogram.bar );
		free( e );
		e = NULL;
		return;

	}

	while ( temp != NULL && e != temp ) {
		prev = temp;
		temp = temp->next;
	}

	if ( e == temp ) {

		prev->next = temp->next;
		clear( e->histogram.bar );
		free( e );
		e = NULL;
		return;

	}
	//Couldn't find the element in the list

}

/*!
 * \brief clear_empty_macs deletes all the Devices of the devices list whose histogram is empty.
 * \param list a pointer to the list head pointer.
 */
void clear_empty_macs( Device ** list ) {

	Device* curr_device = *list;

	while ( curr_device != NULL ) {

		if ( curr_device->histogram.bar == NULL ) {
			delete_Device( list, curr_device );
		}

		curr_device = curr_device->next;

	}

}

/*!
 * \brief get_current_time_ms returns the current time in ms.
 * \return current time in ms.
 */
unsigned long long get_current_time_ms() {

	struct timeval tv;

	gettimeofday(&tv, NULL);
	return (unsigned long long)(tv.tv_sec) * 1000 + (unsigned long long)(tv.tv_usec) / 1000;

}

/*!
 * \brief remove_bar removes one bar from the histogram.
 * \param histogram The histogram to remove the bar from.
 * \param bar The bar to remove from the histogram.
 */
void remove_bar( Rssi_histogram *histogram, Rssi_histogram_bar *bar ) {

	Rssi_histogram_bar *temp = histogram->bar->next, *prev = histogram->bar;

	//The element to remove is the head of the list
	if ( histogram->bar == bar ) {

		histogram->bar = histogram->bar->next;
		clear( bar );
		free( bar );
		bar = NULL;
		return;

	}

	while ( temp != NULL && bar != temp ) {
		prev = temp;
		temp = temp->next;
	}

	if ( bar == temp ) {

		prev->next = temp->next;
		clear( bar );
		free( bar );
		bar = NULL;
		return;

	}
	//Couldn't find the element in the list

}

/*!
* \brief delete_Histogram deletes the histogram.
* \param bar a pointer to the head of the list of histogram bars.
*/
void delete_Histogram( Rssi_histogram_bar *bar ) {

	Rssi_histogram_bar *current = bar, *temp = bar;

	if ( bar == NULL ) {
		return;
	}

	while( current != NULL ) {

		temp = current;
		current = current->next;
		clear( temp );
		free( temp );
		temp = NULL;

	}

}

/*!
* \brief clear removes all occurences in an histogram bar.
* \param bar a pointer to the histogram bars from which to remove all occurences.
*/
void clear( Rssi_histogram_bar * bar ) {

	if ( bar == NULL ) {
		return;
	}

	Occurence *curr_occurence = bar->occurence, *temp_occurence = NULL;

	bar->next = NULL;

	while ( curr_occurence != NULL ) {

		temp_occurence = curr_occurence;
		curr_occurence = curr_occurence->next;
		free( temp_occurence );
		temp_occurence = NULL;

	}

}
