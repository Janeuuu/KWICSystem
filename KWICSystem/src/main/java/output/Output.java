package output;

import sort.Alphabetizer;

import java.util.List;

public interface Output {
    List<String> print(Alphabetizer alphabetizer);
    void WritetoDatebase(Alphabetizer alphabetizer);

    void write(Alphabetizer alphabetizer, String fileName);
}
