`ps -ef |grep pdp-server-1.0-SNAPSHOT.jar |grep java|awk -F " " '{print $2}' | xargs kill  `;
`ps -ef |grep pdp-job-executor-sample-springboot-1.0-SNAPSHOT.jar |grep java|awk -F " " '{print $2}' | xargs kill `;
`ps -ef |grep pdp-report-1.0-SNAPSHOT.jar |grep java|awk -F " " '{print $2}' | xargs kill `;