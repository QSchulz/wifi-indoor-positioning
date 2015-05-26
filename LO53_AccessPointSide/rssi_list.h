#ifndef _RSSI_LIST_
#define _RSSI_LIST_

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <arpa/inet.h>
#include <sys/time.h>

//Default keep delay is 5000ms
#define DEFAULT_KEEP_DELAY 5000

/*
 * Data definitions
 */

 /*!
  * \struct Occurence
  * \brief contains one occurence of a certain rssi value
  */
typedef struct _Occurence {
  unsigned long long deadline; ///< Time after which this sample shall be deleted
  struct _Occurence* next;
} Occurence;

/*!
 * \struct Rssi_histogram_bar
 * \brief contains one bar of the histogram
 */
typedef struct _Rssi_histogram_bar {
  double rssi_mW; ///< rssi_mW RSSI as mW value
  Occurence* occurence; ///< head of list of occurences for rssi_mW RSSI values
  struct _Rssi_histogram_bar* next;
} Rssi_histogram_bar;

/*!
 * \struct Rssi_histogram
 * \brief contains the head of a list of histogram bars
 */
typedef struct {
  Rssi_histogram_bar* bar; ///< head of list of histogram bars
} Rssi_histogram;

/*!
 * \struct Device
 * \brief contains one Device of the Device list and its associated histogram
 */
typedef struct _Device
{
  u_char mac_addr[6]; ///< MAC address in *binary* format
  Rssi_histogram histogram; ///< histogram of all measurements for this device
  struct _Device *next;
} Device;

/*
 * Functions signatures
 */

// General functions
/*!
 * \brief Function string_to_mac converts a human-readable MAC to its binary counterpart.
 * \return the 6-bytes binary MAC
 * \param buf the buffer containing the MAC string
 * \param byte_mac a 6-byte buffer to store the result. byte_mac is returned by the function.
 */
u_char *string_to_mac( char * buf, u_char * byte_mac );

/*!
 * \brief mac_to_string opposite function to string_to_mac.
 * Takes a binary MAC address (such as extracted from IEEE802.11 header by libpcap)
 * and converts it to a human-readable string.
 * \return the string MAC
 * \param byte_mac the binary MAC
 * \param buf an already allocated char buffer, returned by the function.
 */
char * mac_to_string( u_char * byte_mac, char * buf );

// Rssi_sample functions
/*!
 * \brief clear_outdated_values removes all outdated RSSI in the histogram.
 * \param histogram the list from which to filter the outdated values.
 */
void clear_outdated_values( Rssi_histogram * histogram );

/*!
 * \brief add_value adds a new RSSI value in the histogram.
 * \param histogram the histogram to append the RSSI value to.
 * \param value the RSSI value
 */
void add_value( Rssi_histogram * histogram, int value );

// Device functions
/*!
 * \brief clear_list fully clears the devices list
 * When the list is cleared, its value shall be NULL.
 * \param list pointer to the list head pointer.
 */
void clear_list( Device ** list );

/*!
 * \brief find_mac looks up for a MAC address in the list.
 * \return a pointer to the corresponding Device, NULL if not found.
 * \param list the list head pointer.
 * \param mac_value the binary MAC to search.
 */
Device * find_mac( Device * list, u_char * mac_value );

/*!
 * \brief add_Device adds a Device to the list.
 * \return the list head pointer.
 * \param list a pointer to the list head pointer.
 * \param mac_value the binary MAC of the new node to be added.
 */
Device * add_Device( Device ** list, u_char * mac_value );

/*!
 * \brief delete_Device deletes a Device from the list.
 * \param list a pointer to the list head pointer.
 * \param e a pointer to the Device to be deleted.
 */
void delete_Device( Device ** list, Device * e );

/*!
 * \brief clear_empty_macs deletes all the Devices of the devices list whose histogram is empty.
 * \param list a pointer to the list head pointer.
 */
void clear_empty_macs( Device ** list );

/*!
 * \brief get_current_time_ms returns the current time in ms.
 * \return current time in ms.
 */
unsigned long long get_current_time_ms();

/*!
 * \brief remove_bar removes one bar from the histogram.
 * \param histogram The histogram to remove the bar from.
 * \param bar The bar to remove from the histogram.
 */
 void remove_bar( Rssi_histogram * histogram, Rssi_histogram_bar *bar );

 /*!
  * \brief delete_Histogram deletes the histogram.
  * \param bar a pointer to the head of the list of histogram bars.
  */
 void delete_Histogram( Rssi_histogram_bar * bar );

 /*!
  * \brief clear removes all occurences in an histogram bar.
  * \param bar a pointer to the histogram bars from which to remove all occurences.
  */
 void clear( Rssi_histogram_bar * bar );

#endif
