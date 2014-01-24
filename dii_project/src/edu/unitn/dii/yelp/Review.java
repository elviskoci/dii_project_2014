package edu.unitn.dii.yelp;

import java.util.Date;

public class Review implements java.io.Serializable {

	// ReviewID Text CreatedAt BID Likes User_ID Rating

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String review_id;
	private String text;
	private Date created_at;
	private int likes;
	private String user_id;
	private double rating;

	public Review() {

	}
	
	public Review(String id, String text, Date created_at) {

		this.review_id = id;
		this.text = text;
		this.created_at = created_at;
	}
	
	public Review(String id, String text, Date created_at, double rating) {

		this.review_id = id;
		this.text = text;
		this.created_at = created_at;
		this.rating= rating;
	}
	
	public Review(String id, String text, Date created_at, String user_id, double rating) {

		this.review_id = id;
		this.text = text;
		this.created_at = created_at;
		this.rating= rating;
		this.user_id=user_id;
	}
	
	public Review(String id, String text, Date created_at, int likes, String user_id, double rating) {

		this.review_id = id;
		this.text = text;
		this.created_at = created_at;
		this.rating= rating;
		this.user_id=user_id;
		this.likes=likes;
	}
	
	
	public String getReview_id() {
		return review_id;
	}

	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

}
