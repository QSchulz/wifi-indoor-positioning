/*
 * Jérôme COUSSANES
 * jerome.coussanes@utbm.fr
 *
 *
 */
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "fingerprint_functions.h"

int main()
{
    //fingerprint_generation();
    train_fann("friss.dat","friss.net");

    printf("%lf \n",aply_fann("friss.net",0.00923,0));

}
