nohup java -Dfile.encoding=utf-8 -Xss256M -jar ../libs/pdp-server-1.0-SNAPSHOT.jar  --spring.config.location=../conf/pdp-server/ >> pdp-web.log &
nohup java -Dfile.encoding=utf-8 -Xss256M -jar ../libs/pdp-job-executor-sample-springboot-1.0-SNAPSHOT.jar  --spring.config.location=../conf/pdp-job-executor-sample-springboot/ >> pdp-job-executor.log &