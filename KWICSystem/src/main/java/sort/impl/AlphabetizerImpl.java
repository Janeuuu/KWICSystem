package sort.impl;

import shift.CircularShifter;
import sort.Alphabetizer;

// AlphabetizerImpl类实现了Alphabetizer接口，对前面的循环移位结果进行排序
public class AlphabetizerImpl implements Alphabetizer {

    //创建数组但不初始化
    private int sorted_[];

    private CircularShifter shifter_;

    @Override
    public void alpha(CircularShifter shifter) {
        shifter_ = shifter;

        // initialize the index array
        // 初始化索引数组
        sorted_ = new int[shifter_.getLineCount()];
        for(int i = 0; i < sorted_.length; i++)
            sorted_[i] = i;

        // create heap
        // 创建堆
        for(int i = (sorted_.length / 2 - 1); i >= 0; i--)
            siftDown(i, sorted_.length);

        // remove the root and recreate the heap
        // 移除根节点并重新创建堆
        for(int i = (sorted_.length - 1); i >= 1; i--){

            // remove the root
            int tmp = sorted_[0];
            sorted_[0] = sorted_[i];
            sorted_[i] = tmp;

            // recreate the heap
            siftDown(0, i);
        }
    }

    // siftDown方法用于创建堆
    private void siftDown(int root, int bottom){
        int max_child = root * 2 + 1;

        while(max_child < bottom){
            if((max_child + 1) < bottom)
                if(shifter_.getLineAsString(sorted_[max_child + 1]).compareTo(shifter_.getLineAsString(sorted_[max_child])) > 0)
                    max_child++;

            if(shifter_.getLineAsString(sorted_[root]).compareTo(shifter_.getLineAsString(sorted_[max_child])) < 0){
                int tmp = sorted_[root];
                sorted_[root] = sorted_[max_child];
                sorted_[max_child] = tmp;
                root = max_child;
                max_child = root * 2 + 1;
            }else
                break;
        }
    }

    @Override
    public String[] getLine(int line) {
        return shifter_.getLine(sorted_[line]);
    }

    @Override
    public String getLineAsString(int line) {
        return shifter_.getLineAsString(sorted_[line]);
    }

    @Override
    public int getLineCount() {
        return shifter_.getLineCount();
    }
}
