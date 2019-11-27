package adapters;

import java.io.IOException;

public interface BaseAdapter<A, B> {
     B transform(A fromObject) throws IOException;
}
