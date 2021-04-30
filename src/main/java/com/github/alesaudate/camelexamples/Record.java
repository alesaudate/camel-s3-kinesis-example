package com.github.alesaudate.camelexamples;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",")
public class Record {
        @DataField(pos=1) Integer index;
        @DataField(pos=2) Double height;
        @DataField(pos=3) Double width;

        public Integer getIndex() {
                return index;
        }

        public void setIndex(Integer index) {
                this.index = index;
        }

        public Double getHeight() {
                return height;
        }

        public void setHeight(Double height) {
                this.height = height;
        }

        public Double getWidth() {
                return width;
        }

        public void setWidth(Double width) {
                this.width = width;
        }

        @Override public String toString() {
                return "Record{" + "index=" + index + ", height=" + height + ", width=" + width + '}';
        }

}
