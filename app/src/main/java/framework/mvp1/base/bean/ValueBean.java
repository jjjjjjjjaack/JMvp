package framework.mvp1.base.bean;

public class ValueBean extends BaseBean{
    public String name;
    public String value;

    public ValueBean(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }
}
