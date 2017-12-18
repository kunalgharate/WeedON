package com.example.kunalgharate.weedon;

/**
 * Created by Kunal Gharate on 12-12-2017.
 */

public class ServicePojo {


        public String service_name;
        public String service_image;
        public String service_desc;
        public String service_thumbImage;



        public ServicePojo(){

        }

        public ServicePojo(String name, String image, String status, String thumb_image) {
            this.service_name = name;
            this.service_image = image;
            this.service_desc = status;
            this.service_thumbImage = thumb_image;
        }

        public String getName() {
            return service_name;
        }

        public void setName(String name) {
            this.service_name = name;
        }

        public String getImage() {
            return service_image;
        }

        public void setImage(String image) {
            this.service_image = image;
        }

        public String getStatus() {
            return service_desc;
        }

        public void setStatus(String status) {
            this.service_desc = status;
        }

        public String getThumb_image() {
            return service_thumbImage;
        }

        public void setThumb_image(String thumb_image) {
            this.service_thumbImage = thumb_image;
        }

    }