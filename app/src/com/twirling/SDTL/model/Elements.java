package com.twirling.SDTL.model;

import java.util.List;

/**
 * Created by 谢秋鹏 on 2016/6/24.
 */
public class Elements {
    private List<ImageBean> images;
    /**
     * id : 002
     * src : Videos/video1.mp4
     * name : video1
     * type : 0
     * distance : 1
     * azimuth : 0
     * elvation : 0
     * size : 1
     * startTime : 0
     * endTime : 30
     * trigger : 0
     */
    private List<VideoBean> videos;
    /**
     * id : 003
     * src : Sound/sound1.wav
     * name : sound1
     * type : 3
     * distance : 1
     * azimuth : 110
     * elvation : 0
     * size : 1
     * startTime : 20
     * endTime : 30
     * trigger : 0
     * channels : 3
     */
    private List<SoundGroupBean> sound_groups;

    public List<ImageBean> getImages() {
        return images;
    }

    public void setImages(List<ImageBean> images) {
        this.images = images;
    }

    public List<VideoBean> getVideo() {
        return videos;
    }

    public void setVideo(List<VideoBean> videos) {
        this.videos = videos;
    }

    public List<SoundGroupBean> getSound_group() {
        return sound_groups;
    }

    public void setSound_group(List<SoundGroupBean> sound_groups) {
        this.sound_groups = sound_groups;
    }

    public static class ImageBean {
        private String id;
        private String src;
        private String name;
        private int type;
        private double distance;
        private int azimuth;
        private int elvation;
        private int size;
        private int startTime;
        private int endTime;
        private int trigger;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public int getAzimuth() {
            return azimuth;
        }

        public void setAzimuth(int azimuth) {
            this.azimuth = azimuth;
        }

        public int getElvation() {
            return elvation;
        }

        public void setElvation(int elvation) {
            this.elvation = elvation;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getTrigger() {
            return trigger;
        }

        public void setTrigger(int trigger) {
            this.trigger = trigger;
        }
    }

    public static class VideoBean {
        private String id;
        private String src;
        private String name;
        private int type;
        private int distance;
        private int azimuth;
        private int elvation;
        private int size;
        private int startTime;
        private int endTime;
        private int trigger;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getAzimuth() {
            return azimuth;
        }

        public void setAzimuth(int azimuth) {
            this.azimuth = azimuth;
        }

        public int getElvation() {
            return elvation;
        }

        public void setElvation(int elvation) {
            this.elvation = elvation;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getTrigger() {
            return trigger;
        }

        public void setTrigger(int trigger) {
            this.trigger = trigger;
        }
    }

    public static class SoundGroupBean {
        private String id;
        private String src;
        private String name;
        private int type;
        private int distance;
        private int azimuth;
        private int elvation;
        private int size;
        private int startTime;
        private int endTime;
        private int trigger;
        private int channels;
        private int profileID;
        private String metadata;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getAzimuth() {
            return azimuth;
        }

        public void setAzimuth(int azimuth) {
            this.azimuth = azimuth;
        }

        public int getElvation() {
            return elvation;
        }

        public void setElvation(int elvation) {
            this.elvation = elvation;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getTrigger() {
            return trigger;
        }

        public void setTrigger(int trigger) {
            this.trigger = trigger;
        }

        public int getChannels() {
            return channels;
        }

        public void setChannels(int channels) {
            this.channels = channels;
        }

        public String getMetadata() {
            return metadata;
        }

        public void setMetadata(String metadata) {
            this.metadata = metadata;
        }

        public int getProfileID() {
            return profileID;
        }

        public void setProfileID(int profileID) {
            this.profileID = profileID;
        }
    }
}