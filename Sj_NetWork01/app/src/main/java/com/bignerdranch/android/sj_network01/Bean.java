package com.bignerdranch.android.sj_network01;

import java.util.List;

/**
 * Created by pawN（沈杰） on 2019/5/10.
 * http://www.imooc.com/api/teacher?type=2&page=1
 * 不需要构造方法
 */
public class Bean {

    /**
     * 1.status 2.datas列表
     */
    private int mStatus;
    private List<data> mDatas;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public List<data> getDatas() {
        return mDatas;
    }

    public void setDatas(List<data> datas) {
        mDatas = datas;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "status=" + mStatus +
                ", datas=" + mDatas +
                '}';
    }

    /**
     * 自定义一个静态类data类型
     */
    public static class data{
        private int mID;
        private int mLearnerNumber;
        private String mName;
        private String mSmallPictureUrl;
        private String mBigPictureUrl;
        private String mDescription;

        public int getID() {
            return mID;
        }

        public void setID(int ID) {
            mID = ID;
        }

        public int getLearnerNumber() {
            return mLearnerNumber;
        }

        public void setLearnerNumber(int learnerNumber) {
            mLearnerNumber = learnerNumber;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getSmallPictureUrl() {
            return mSmallPictureUrl;
        }

        public void setSmallPictureUrl(String smallPictureUrl) {
            mSmallPictureUrl = smallPictureUrl;
        }

        public String getBigPictureUrl() {
            return mBigPictureUrl;
        }

        public void setBigPictureUrl(String bigPictureUrl) {
            mBigPictureUrl = bigPictureUrl;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        @Override
        public String toString() {
            return "data{" +
                    "mID=" + mID +
                    ", mLearnerNumber=" + mLearnerNumber +
                    ", mName='" + mName + '\'' +
                    ", mSmallPictureUrl='" + mSmallPictureUrl + '\'' +
                    ", mBigPictureUrl='" + mBigPictureUrl + '\'' +
                    ", mDescription='" + mDescription + '\'' +
                    '}';
        }
    }
}
