package adapters;

public interface BaseAdapter<A, B> {
     B transform(A fromObject);
}
