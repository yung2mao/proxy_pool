package cn.whitetown.modo;

import java.util.Collection;
import java.util.HashSet;

/**
 * 代理集合
 * @author: taixian
 * @date: created in 2021/01/04
 */
public class ProxySet extends HashSet<OwnProxy> {

    @Override
    public boolean add(OwnProxy proxy) {
        synchronized (this) {
            return super.add(proxy);
        }
    }

    @Override
    public boolean addAll(Collection<? extends OwnProxy> c) {
        synchronized (this) {
            return super.addAll(c);
        }
    }
}
