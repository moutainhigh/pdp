start cmd /c "title pdp-web && java -Dfile.encoding=utf-8 -jar ../libs/pdp-server-1.0-SNAPSHOT.jar  --spring.config.location=../conf/pdp-server/ "
start cmd /c "title pdp-job-executor && java -Dfile.encoding=utf-8 -jar ../libs/pdp-job-executor-sample-springboot-1.0-SNAPSHOT.jar  --spring.config.location=../conf/pdp-job-executor-sample-springboot/ "

pause