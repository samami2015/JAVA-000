package auto.load;

import domian.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Import(AnnotationDemo.Config.class)
public class AnnotationDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDemo.class);
        applicationContext.refresh();
        System.out.println(applicationContext.getBeansOfType(User.class));
        applicationContext.close();
    }


    @Component
    public static class Config {
        @Bean
        public User user() {
            User user = new User();
            user.setDesc("@Bean 方式");
            user.setName("ws");
            user.setId(System.currentTimeMillis());
            return user;
        }
    }
}
