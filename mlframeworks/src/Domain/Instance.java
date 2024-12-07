package Domain;

import java.util.ArrayList;
import java.util.List;

public  class Instance <F,L>{
    private List<F> input;
    private L output;

    public List<F> getInput(){
        return input;
    }

    public L getOutput(){
        return output;
    }
    public void setInput(List<F> input){
        this.input =  input;
    }
    public void setOutput(L output){
        this.output = output;
    }
}
