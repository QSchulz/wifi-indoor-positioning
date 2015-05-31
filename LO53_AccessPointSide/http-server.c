#include "http-server.h"

int endpoints (void *cls, struct MHD_Connection *connection, const char *url, const char *method, const char *version, const char *upload_data, size_t *upload_data_size, void **cons_cls) {

  if (got_sigint == 1) {
    MHD_stop_daemon( mhd_daemon );
  }

  struct MHD_Response *response;
  int ret;
  char* url_args;
  char* page;

  const char *url_arguments = MHD_lookup_connection_value( connection, MHD_GET_ARGUMENT_KIND, "mac_addrs");

  if ( url_arguments == NULL || strlen( url_arguments ) == 0 ) {
    return MHD_NO;
  }

  url_args = malloc( sizeof( char ) * ( strlen( url_arguments ) + 1 ) );
  strcpy( url_args, url_arguments );

  sem_wait( &synchro );

  page = build_buffer_full( url_args, "AP-MAC" );

  sem_post( &synchro );

  response = MHD_create_response_from_buffer( strlen(page), page, MHD_RESPMEM_MUST_FREE );
  MHD_add_response_header( response, "Content-Type", "application/json" );
  ret = MHD_queue_response( connection, MHD_HTTP_OK, response );
  MHD_destroy_response( response );

  free( url_args );

  return ret;

}

int start_http_server(){

  mhd_daemon = MHD_start_daemon( MHD_USE_SELECT_INTERNALLY, PORT, NULL, NULL, endpoints, NULL, MHD_OPTION_END );

  if ( mhd_daemon == NULL ) {
    return -1;
  }

  return 0;

}

// Communications functions
/*!
 * \brief build_Device builds the HTTP response part (part of the json sentence) for Device e.
 * This function is used by build_buffer and build_buffer_full.
 * \return the json fragment string.
 * \param e the Device whose data is required.
 * \param buf the string buffer to store the json fragment, returned by the function.
 */
char * build_Device(Device * e, char * buf) {
	return NULL;

}

/*!
 * \brief build_buffer builds the full json sentence based on the positioning server request parameters.
 * \return the json message.
 * \param list the devices list.
 * \param buffer the string buffer to store the response, returned by this function.
 * \param my_name the human readable MAC of the access point.
 * \param macs_requested the list of MAC addresses requested by the server, binary format. Its length equals 6*nb_macs.
 * \param nb_macs the number of mac requested.
 */
char * build_buffer(char* buffer, char ** mac_addresses, unsigned short nb_macs) {

  int i = 0, j = 0, nb_occurences = 0, nb_total_occurences = 0;
  Rssi_histogram_bar *curr_bar = NULL;
  Occurence *curr_occurence = NULL;
  Device *device;
  u_char mac[6];

  for ( i = 0; i < nb_macs; i++ ) {

    if ( i != 0 ) {
      strcat( buffer, "," );
    }

    sprintf( buffer + strlen( buffer ), "{\"mac\":\"%s\",\"histogram\":[", mac_addresses[i] );

    device = find_mac( *device_list, string_to_mac( mac_addresses[i], mac ) );

    if ( device != NULL ) {

      clear_outdated_values( &device->histogram );

      curr_bar = device->histogram.bar;

      nb_total_occurences = 0;

      while ( curr_bar != NULL ) {

        nb_occurences = 0;
        curr_occurence = curr_bar->occurence;

        if ( curr_bar != device->histogram.bar ) {
          strcat( buffer, "," );
        }

        while( curr_occurence != NULL ) {

          nb_occurences++;
          curr_occurence = curr_occurence->next;

        }

        nb_total_occurences += nb_occurences;
        sprintf( buffer + strlen( buffer ), "{\"rssi\":%f,\"occurences\":%d}", curr_bar->rssi_mW, nb_occurences );
        curr_bar = curr_bar->next;

      }

    }
    sprintf( buffer + strlen( buffer ), "],\"total_occurences\":%d}", nb_total_occurences );

  }

  return buffer;
}

/*!
 * \brief build_buffer_full generated the json response for all the device list content.
 * \return the json response looking like that:
 * {"mac_ap":"AP-MAC","results":[{"mac":"XX:XX:XX:XX:XX:XX","histogram":[],"total_occurences":0},{"mac":"YY:YY:YY:YY:YY:YY","histogram":[{"rssi":2.511886,"occurences":1}],"total_occurences":1}]}
 * \param list the devices list.
 * \param my_name the human readable MAC of the access point.
 */
char * build_buffer_full(char * buffer, char * my_name) {

  char **mac_addresses;
  char* page = malloc( sizeof( char ) * 500 );
  int i = 0;
  int size = round( strlen( buffer )/18. );

  mac_addresses = malloc( sizeof( char* ) * size );
  mac_addresses[0] = malloc( sizeof( char ) * 17 );
  mac_addresses[0] = strtok( buffer, "," );

  for ( i = 1; i < size; i++ ) {
    mac_addresses[i] = malloc( sizeof( char ) * 17 );
    mac_addresses[i] = strtok( NULL, "," );
  }

  sprintf( page, "{\"mac_ap\":\"%s\",\"results\":[", my_name );
  build_buffer( page, mac_addresses, size );
  strcat( page, "]}" );

  return page;

}
