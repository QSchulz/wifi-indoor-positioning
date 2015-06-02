CURR_DIR=$(dir $(abspath $(lastword $(MAKEFILE_LIST))))
PREFIX=$(CURR_DIR)staging_dir
export STAGING_DIR=$(CURR_DIR)staging_dir
GCC=$(PREFIX)/toolchain/bin/mips-openwrt-linux-gcc
LD=$(PREFIX)/toolchain/bin/mips-openwrt-linux-ld
GCCFLAGS=-Wall -Werror -O2 -I$(PREFIX)/toolchain/include/ -I$(PREFIX)/target/usr/include/
LDFLAGS=-L$(PREFIX)/toolchain/lib/ -L$(PREFIX)/target/usr/lib/ -ldl -lpcap -lm -pthread -lcurl -lmicrohttpd -lz -lssl -lcrypto -lpolarssl

TARGET=rssi-display

all: $(TARGET)

%.o : %.c %.h
	$(GCC) $(GCCFLAGS) -o $@ -c $<

rssi-display: rssi-display.o rssi_list.o pcap-thread.o http-server.o http-client.o
	$(GCC) $(LDFLAGS) -o rssi-display rssi-display.o rssi_list.o pcap-thread.o http-server.o http-client.o

clean:
	rm -f *.o
	rm -f rssi-display
	rm -f *~
