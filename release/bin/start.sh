nohup java -Dfile.encoding=utf-8 -Xms256M -Xmx2048M -jar ../libs/pdp-server-1.0-SNAPSHOT.jar  --spring.config.location=../conf/pdp-server/ >> /dev/null &
nohup java -Dfile.encoding=utf-8 -Xms256M -Xmx1024M -jar ../libs/pdp-job-executor-sample-springboot-1.0-SNAPSHOT.jar  --spring.config.location=../conf/pdp-job-executor-sample-springboot/ >> /dev/null &
nohup java -Dfile.encoding=utf-8 -Xms256M -Xmx1024M -jar ../libs/pdp-report-1.0-SNAPSHOT.jar  --spring.config.location=../conf/pdp-report/ >> /dev/null &