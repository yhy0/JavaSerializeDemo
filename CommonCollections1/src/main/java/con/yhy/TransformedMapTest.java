package con.yhy;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.TransformedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yhy
 * @date 2021/6/8 21:25
 * @github https://github.com/yhy0
 */

// 当map 中的 key 变动时，输出一句话
class KeyUpdate implements Transformer {
    @Override
    public Object transform(Object o) {
        System.out.println("key update");
        return null;
    }
}

public class TransformedMapTest {

    public static void main(String[] args) throws Exception {
        Map innerMap = new HashMap();

        Transformer transformer = new KeyUpdate();

        Map outerMap = TransformedMap.decorate(innerMap, transformer, null);
        outerMap.put("test", "xxxx");
    }
}
