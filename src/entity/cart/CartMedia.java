package entity.cart;

import entity.media.Media;

/**
 * Thỏa mãn Single Responsibility Principle - SRP vì nó chỉ có trách nhiệm duy nhất là quản lý thông tin về một mặt hàng trong giỏ hàng.
 * Thỏa mãn Open/Closed Principle - OCP vì nó có thể mở rộng (thông qua kế thừa hoặc thực thi giao diện) mà không cần sửa đổi mã nguồn.
 */

public class CartMedia {
    
    private Media media;
    private int quantity;
    private int price;

    public CartMedia(){

    }

    public CartMedia(Media media, Cart cart, int quantity, int price) {
        this.media = media;
        this.quantity = quantity;
        this.price = price;
    }
    
    public Media getMedia() {
        return this.media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" 
            + " media='" + media + "'" 
            + ", quantity='" + quantity + "'" 
            + "}";
    }

}

    
