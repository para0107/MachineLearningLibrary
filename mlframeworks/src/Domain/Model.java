package Domain;

import java.util.ArrayList;
import java.util.List;

public interface Model<F, L>  {
   public void train(List<Instance<F, L>> instances);


    public List<L> test(List<Instance<F,L>> instances);


}
