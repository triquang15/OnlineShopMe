package com.triquang.review.vote;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triquang.common.entity.Customer;
import com.triquang.common.entity.Review;
import com.triquang.common.entity.ReviewVote;
import com.triquang.review.ReviewRepository;

@Service
public class ReviewVoteService {
	
	@Autowired private ReviewVoteRepository voteRepository;
	@Autowired private ReviewRepository reviewRepository;
	
	public VoteResult undoVote(ReviewVote vote, Integer reviewId, VoteType voteType) {
		voteRepository.delete(vote);
		reviewRepository.updateVoteCount(reviewId);
		Integer voteCount = reviewRepository.getVoteCount(reviewId);
		
		return VoteResult.success("You have unvoted " + voteCount + " that review.", voteCount);
	}
	
	public VoteResult doVote(Integer reviewId, Customer customer, VoteType voteType) {
		Review review = null;
		
		try {
			 review = reviewRepository.findById(reviewId).get();
		} catch (NoSuchElementException e) {
			return VoteResult.fail("The review ID " + reviewId + " no longer exists");
		}
		
		ReviewVote vote = voteRepository.findByReviewAndCustomer(reviewId, customer.getId());
		
		if(vote != null) {
			if(vote.isUpvoted() && voteType.equals(VoteType.UP) ||
					vote.isDownvoted() && voteType.equals(VoteType.DOWN)) {
				return undoVote(vote, reviewId, voteType);
			} else if (vote.isUpvoted() && voteType.equals(VoteType.DOWN)) {
				vote.voteDown();
			} else if (vote.isDownvoted() && voteType.equals(VoteType.UP)) {
				vote.voteUp();
			}
		} else {
			vote = new ReviewVote();
			vote.setCustomer(customer);
			vote.setReview(review);
			
			if(voteType.equals(VoteType.UP)) {
				vote.voteUp();
			} else {
				vote.voteDown();
			}
		}
		
		voteRepository.save(vote);
		reviewRepository.updateVoteCount(reviewId);
		Integer voteCount = reviewRepository.getVoteCount(reviewId);
		
		return VoteResult.success("You have successfully voted " + voteType + " that review.", voteCount);
		
	}
}
