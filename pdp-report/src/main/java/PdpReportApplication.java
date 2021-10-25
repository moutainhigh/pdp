import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/21
 */

@SpringBootApplication(scanBasePackages = {"org.jeecg.modules.jmreport"})
public class PdpReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(PdpReportApplication.class, args);
    }
}
