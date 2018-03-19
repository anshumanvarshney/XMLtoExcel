
public class JsoupParser {

    private int baseline;
    private int b;
    private int t;
    private int l;
    private int r;
    private String text;

    public JsoupParser(int baseline, int b, int t, int l, int r, String text) {
        this.baseline = baseline;
        this.b = b;
        this.t = t;
        this.l = l;
        this.r = r;
        this.text = text;
    }

    public int getBaseline() {
        return baseline;
    }

    public int getB() {
        return b;
    }

    public int getT() {
        return t;
    }

    public int getL() {
        return l;
    }

    public int getR() {
        return r;
    }

    public String getText() {
        return text;
    }
}



