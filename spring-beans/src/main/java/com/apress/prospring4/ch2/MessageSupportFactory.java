package com.apress.prospring4.ch2;

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class MessageSupportFactory {
    private static MessageSupportFactory instance;

    private Properties props;
    private MessageRenderer renderer;
    private MessageProvider provider;

    private MessageSupportFactory() {
        props = new Properties();

        try {
            if (true) {
                System.out.println(this.getClass() == MessageSupportFactory.class);

                //java-msf.properties 正常情况下不会被拷贝到com/apress/prospring4/ch2/目录下，此时会报错
                props.load(ClassLoader.getSystemResourceAsStream("com/apress/prospring4/ch2/java-msf.properties"));

                //1:
                // props.load(this.getClass().getResourceAsStream("/res-msf.properties"));

                //2:
                // props.load(ClassLoader.getSystemResourceAsStream("res-msf.properties"));

                //3:
                // props.load(ClassLoader.getSystemClassLoader().getResourceAsStream("res-msf.properties"));

                String rendererClass = props.getProperty("renderer.class");
                String providerClass = props.getProperty("provider.class");
                renderer = (MessageRenderer) Class.forName(rendererClass).newInstance();
                provider = (MessageProvider) Class.forName(providerClass).newInstance();
            }


            if (false) {
                ResourceBundle bundle = ResourceBundle.getBundle("res-msf");
                String rendererClass = bundle.getString("renderer.class");
                String providerClass = bundle.getString("provider.class");
                renderer = (MessageRenderer) Class.forName(rendererClass).newInstance();
                provider = (MessageProvider) Class.forName(providerClass).newInstance();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    static {
        instance = new MessageSupportFactory();
    }

    public static MessageSupportFactory getInstance() {
        return instance;
    }

    public MessageRenderer getMessageRenderer() {
        return renderer;
    }

    public MessageProvider getMessageProvider() {
        return provider;
    }
}
