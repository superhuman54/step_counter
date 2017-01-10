package com.kimkihwan.me.stepcounter.service;

import java.util.List;

/**
 * Created by jamie on 10/3/16.
 */

public class GeocodeResponse {

    Result result;

    public Result getResult() {
        return result;
    }

    public GeocodeResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    class Result {

        int total;
        String userquery;
        List<Address> items;

        public int getTotal() {
            return total;
        }

        public Result setTotal(int total) {
            this.total = total;
            return this;
        }

        public String getUserquery() {
            return userquery;
        }

        public Result setUserquery(String userquery) {
            this.userquery = userquery;
            return this;
        }

        public List<Address> getItems() {
            return items;
        }

        public Result setItems(List<Address> items) {
            this.items = items;
            return this;
        }

        class Address {

            String address;
            Detail addrdetail;
            boolean isRoadAddress;
            Point point;

            public String getAddress() {
                return address;
            }

            public Address setAddress(String address) {
                this.address = address;
                return this;
            }

            public Detail getAddrdetail() {
                return addrdetail;
            }

            public Address setAddrdetail(Detail addrdetail) {
                this.addrdetail = addrdetail;
                return this;
            }

            public boolean isRoadAddress() {
                return isRoadAddress;
            }

            public Address setRoadAddress(boolean roadAddress) {
                isRoadAddress = roadAddress;
                return this;
            }

            public Point getPoint() {
                return point;
            }

            public Address setPoint(Point point) {
                this.point = point;
                return this;
            }

            class Detail {
                String country;
                String sido;
                String sigugun;
                String dongmyun;
                String rest;

                public String getCountry() {
                    return country;
                }

                public Detail setCountry(String country) {
                    this.country = country;
                    return this;
                }

                public String getSido() {
                    return sido;
                }

                public Detail setSido(String sido) {
                    this.sido = sido;
                    return this;
                }

                public String getSigugun() {
                    return sigugun;
                }

                public Detail setSigugun(String sigugun) {
                    this.sigugun = sigugun;
                    return this;
                }

                public String getDongmyun() {
                    return dongmyun;
                }

                public Detail setDongmyun(String dongmyun) {
                    this.dongmyun = dongmyun;
                    return this;
                }

                public String getRest() {
                    return rest;
                }

                public Detail setRest(String rest) {
                    this.rest = rest;
                    return this;
                }
            }

            class Point {
                double x;
                double y;

                public double getX() {
                    return x;
                }

                public Point setX(double x) {
                    this.x = x;
                    return this;
                }

                public double getY() {
                    return y;
                }

                public Point setY(double y) {
                    this.y = y;
                    return this;
                }
            }
        }
    }
}
