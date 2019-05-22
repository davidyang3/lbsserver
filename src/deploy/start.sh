root=`dirname \`pwd\``
APPLICATION=$root/bin/lbsserver.jar
SPRING_CONFIG_FILE=../conf/application.yml
INFO_LOG_FILE_PATH=../log/info/lbsserver.info.log
ERROR_LOG_FILE_PATH=../log/error/lbsserver.errorlog
WARN_LOG_FILE_PATH=../log/warn/lbsserver.warn.log
DEBUG_LOG_FILE_PATH=../log/debug/lbsserver.debug.log
ACCESS_LOG_FILE_PATH=../log/access/lbsserver.access.log
ACCESS_DEBUG_LOG_FILE_PATH=../log/access_debug/lbsserver.access_debug.log
LOGBACK_FILE_PATH=../conf/logback.xml

nohup java -Dspring.config.location=$SPRING_CONFIG_FILE -Dfile.encoding=UTF-8 \
-Dlogging.config=$LOGBACK_FILE_PATH \
-Dlogging.info_log_file_path=$INFO_LOG_FILE_PATH -Dlogging.error_log_file_path=$ERROR_LOG_FILE_PATH \
-Dlogging.warn_log_file_path=$WARN_LOG_FILE_PATH -Dlogging.debug_log_file_path=$DEBUG_LOG_FILE_PATH \
-Dlogging.access_log_file_path=$ACCESS_LOG_FILE_PATH -Dlogging.access_debug_log_file_path=$ACCESS_DEBUG_LOG_FILE_PATH \
-Dlogging.info_log_max_history_in_hours=168 -Dlogging.error_log_max_history_in_days=30 \
-Dlogging.warn_log_max_history_in_days=30 -Dlogging.debug_log_max_history_in_days=7 \
-Dlogging.access_log_max_history_in_hours=168 -Dlogging.access_debug_log_max_history_in_days=3 \
-jar $APPLICATION > /dev/null 2>&1 &