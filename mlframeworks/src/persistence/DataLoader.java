package persistence;

import Domain.Instance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public static List<Instance<Double, Integer>> loadCSV(File filepath)
    {
        List<Instance<Double , Integer>> data = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filepath)))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                String[] parts = line.split(",");
                Instance<Double, Integer> instance = new Instance<>();
                List<Double> input = new ArrayList<>();
                for(int i = 0; i < parts.length - 1; i++)
                {
                    input.add(Double.parseDouble(parts[i]));
                }
                instance.setInput(input);
                instance.setOutput(Integer.parseInt(parts[parts.length - 1]));
                data.add(instance);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;
    }
}
