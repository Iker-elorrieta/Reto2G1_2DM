package serverSocket;

import org.hibernate.proxy.HibernateProxy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class HibernateProxyExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        // No excluir campos basándose en su tipo
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        // Excluir clases que son proxies de Hibernate
        return clazz.equals(HibernateProxy.class) || 
               HibernateProxy.class.isAssignableFrom(clazz);
    }
}
