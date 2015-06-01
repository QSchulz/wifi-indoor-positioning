#include "http-client.h"

void *http_client_function( void *arg ) {

  Message msg;
  char mac[18];
  const char* post_name = "DeviceMACAddress=";
  char *post;
  CURL* curl;

  post = malloc( sizeof( char ) * 36 );

  curl = curl_easy_init();
  curl_easy_setopt( curl, CURLOPT_URL, "http://192.168.2.150/" );
  curl_easy_setopt( curl, CURLOPT_POSTFIELDSIZE, sizeof( char ) * 36 );

  while( got_sigint == 0 ) {

    strcpy( post, post_name );
    //This will block thread until a message is received
    msgrcv( msqid, &msg, sizeof( Message ) - 4, 53, 0 );
    strcat( post, mac_to_string( msg.mac, mac ) );
    curl_easy_setopt( curl, CURLOPT_COPYPOSTFIELDS, post );
    //Be careful, this call has thread-blocking ability.
    curl_easy_perform( curl );

  }

  curl_easy_cleanup( curl );
  curl_global_cleanup();

  free( post );
  free( curl );

  pthread_exit( (void *) 0 );

}
