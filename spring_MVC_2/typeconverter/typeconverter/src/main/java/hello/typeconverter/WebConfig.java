package hello.typeconverter;

import hello.typeconverter.convert.IntegerToStringConverter;
import hello.typeconverter.convert.IpPortToStringConverter;
import hello.typeconverter.convert.StringToIntegerConverter;
import hello.typeconverter.convert.StringToIpPortConverter;
import hello.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new IntegerToStringConverter());
//        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IpPortToStringConverter());
        registry.addConverter(new StringToIpPortConverter());

        registry.addFormatter(new MyNumberFormatter());
    }
}
