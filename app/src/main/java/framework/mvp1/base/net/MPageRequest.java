package framework.mvp1.base.net;

import java.util.Map;

public class MPageRequest extends BaseRequest {

    public String offset;
    public String limit;

    @Override
    public Map<String, Object> bulitReqMap() {
        Map map = super.bulitReqMap();
        map.put("offset", offset);
        map.put("limit", limit);
        return map;
    }
}
