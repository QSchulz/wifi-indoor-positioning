#ifndef _HTTP_SERVER_
#define _HTTP_SERVER_

#include <semaphore.h>
#include <microhttpd.h>
#include <sys/signal.h>
#include <math.h>
#include "rssi_list.h"

extern volatile sig_atomic_t got_sigint;
extern Device ** device_list;
extern sem_t synchro;
struct MHD_Daemon* mhd_daemon;
#define PORT 80

int endpoints(void *cls, struct MHD_Connection *connection, const char *url, const char *method, const char *version, const char *upload_data, size_t *upload_data_size, void **cons_cls);

int start_http_server();

// Communications functions
/*!
 * \brief build_Device builds the HTTP response part (part of the json sentence) for Device e.
 * This function is used by build_buffer and build_buffer_full.
 * \return the json fragment string.
 * \param e the Device whose data is required.
 * \param buf the string buffer to store the json fragment, returned by the function.
 */
char * build_Device(Device * e, char * buf);

/*!
 * \brief build_buffer builds the full json sentence based on the positioning server request parameters.
 * \return the json message.
 * \param list the devices list.
 * \param buffer the string buffer to store the response, returned by this function.
 * \param my_name the human readable MAC of the access point.
 * \param macs_requested the list of MAC addresses requested by the server, binary format. Its length equals 6*nb_macs.
 * \param nb_macs the number of mac requested.
 */
char * build_buffer(char* buffer, char ** macs_requested, unsigned short nb_macs);

/*!
 * \brief build_buffer_full generated the json response for all the device list content.
 * \return the json response looking like that:
 * {"mac_ap":"AP-MAC","results":[{"mac":"XX:XX:XX:XX:XX:XX","histogram":[],"total_occurences":0},{"mac":"YY:YY:YY:YY:YY:YY","histogram":[{"rssi":2.511886,"occurences":1}],"total_occurences":1}]}
 * \param list the devices list.
 * \param my_name the human readable MAC of the access point.
 */
char * build_buffer_full(char * buffer, char * my_name);

#endif //_HTTP_SERVER_
