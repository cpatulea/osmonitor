#include "osmonitor.h"

/* file system */
#define CACHE_PATH "/cache"
#define SDCARD_PATH "/sdcard"
#define SYSTEM_PATH "/system"
#define DATA_PATH "/data"

/* power */
#define AC_ONLINE_PATH "/sys/class/power_supply/ac/online"
#define USB_ONLINE_PATH "/sys/class/power_supply/usb/online"
#define BATTERY_STATUS_PATH "/sys/class/power_supply/battery/status"
#define BATTERY_HEALTH_PATH "/sys/class/power_supply/battery/health"
#define BATTERY_CAPACITY_PATH "/sys/class/power_supply/battery/capacity"
#define BATTERY_TECHNOLOGY_PATH "/sys/class/power_supply/battery/technology"
#define BATTERY_VOLTAGE_PATH "/sys/class/power_supply/battery/batt_vol"
#define BATTERY_TEMPERATURE_PATH "/sys/class/power_supply/battery/batt_temp"

/* cpu */
#define CPUINFO_MAX "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq"
#define CPUINFO_MIN "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_min_freq"
#define CPU_SCALING_CUR "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_cur_freq"
#define CPU_SCALING_MAX "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_max_freq"
#define CPU_SCALING_MIN "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_min_freq"
#define CPU_SCALING_GOR "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_governor"

#define AKM_DEVICE_NAME "/dev/akm8976_aot"
#define OMAP_TEMPERATURE "/sys/class/hwmon/hwmon0/device/temp1_input"
#define TEGRA3_TEMPERATURE "/sys/kernel/debug/tegra_thermal/temp_tj"
#define EVENT_TYPE_TEMPERATURE ABS_THROTTLE

#define ID_T  (3)
#define SENSORS_TEMPERATURE    (1<<ID_T)

/* CPU INFO */
typedef struct processor_proto {
	unsigned int cpumax, cpumin, scalmax, scalmin, scalcur;
	char scalgov[BUFFERSIZE];
} processor_info;

void misc_dump_processor();
int misc_get_processor_cpumax(int num);
int misc_get_processor_cpumin(int num);
int misc_get_processor_scalcur(int num);
int misc_get_processor_scalmax(int num);
int misc_get_processor_scalmin(int num);
void misc_get_processor_scalgov(int num, char* buf);
int misc_get_processor_number();
int misc_get_processor_omaptemp();

/* Battery Module */
typedef struct power_proto
{
	char status[16];
	char health[16];
	char technology[16];
	int capacity;
	int voltage;
	int temperature;
	int aconline;
	int usbonline;
} power_info;

void misc_dump_power();
int misc_get_power_capacity();
int misc_get_power_voltage();
int misc_get_power_temperature();
int misc_get_power_aconline();
int misc_get_power_usbonline();
void misc_get_power_health(char *buf);
void misc_get_power_status(char *buf);
void misc_get_power_technology(char *buf);

void misc_dump_filesystem();
double misc_get_filesystem_systemtotal();
double misc_get_filesystem_datatotal();
double misc_get_filesystem_sdcardtotal();
double misc_get_filesystem_cachetotal();
double misc_get_filesystem_systemused();
double misc_get_filesystem_dataused();
double misc_get_filesystem_sdcardused();
double misc_get_filesystem_cacheused();
double misc_get_filesystem_systemavail();
double misc_get_filesystem_dataavail();
double misc_get_filesystem_sdcardavail();
double misc_get_filesystem_cacheavail();
