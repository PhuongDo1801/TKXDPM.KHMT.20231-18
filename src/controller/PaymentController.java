package controller;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import common.exception.InvalidCardException;
import common.exception.PaymentException;
import common.exception.UnrecognizedException;
import entity.cart.Cart;
import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;
import subsystem.InterbankInterface;
import subsystem.InterbankSubsystem;


/**
 * This {@code PaymentController} class control the flow of the payment process
 * in our AIMS Software.
 *
 * @author manhphuong
 *
 * Không thỏa mãn Single Responsibility Principle - SRP bởi vì trong lớp có 3 hàm là getExpirationDate, payOrder, emptyCart nó thực hiện
 * mục đích khác nhau nhưng được nằm chung 1 file
 * Giải pháp cho vấn đề này: nên để getExpirationDate ở class CartController vì getExpirationDate thực hiện mục đích lấy ngày hết hạn của Cart, getExpirationDate thì vẫn để
 * ở class PaymentController, emptyCart để ở class CartController vì emptyCart thực hiện mục đích lấy làm trống Cart
 * Thỏa mãn Liskov Substitution Principle - LSP vì PaymentController vì PaymentController có thể thay thế được cho class BaseController
 *
 *
 *
 *
 */
public class PaymentController extends BaseController {

	/**
	 * Represent the card used for payment
	 */
	private CreditCard card;

	/**
	 * Represent the Interbank subsystem
	 */
	private InterbankInterface interbank;

	/**
	 * Validate the input date which should be in the format "mm/yy", and then
	 * return a {@link java.lang.String String} representing the date in the
	 * required format "mmyy" .
	 *
	 * @param date - the {@link java.lang.String String} represents the input date
	 * @return {@link java.lang.String String} - date representation of the required
	 *         format
	 * @throws InvalidCardException - if the string does not represent a valid date
	 *                              in the expected format
	 *
     * Coupling
	 * The method getExpirationDate directly processes and validates the format of the credit card expiration date.
	 * There is a level of coupling with the content structure of the expiration date.
	 * This is an example of Content Coupling.
	 *
     * Cohesion
	 * Functional Cohesion: as it primarily focuses on a specific function: processing and validating the expiration date of a credit card. Every line of code in this function
     * contributes to this goal, from analyzing the input string format, checking for valid conditions, to handling potential exceptions.
     * Procedural Cohesion: This function shares data through the date parameter and uses month, year, and expirationDate variables to store and process data.
	 */
	private String getExpirationDate(String date) throws InvalidCardException {
		String[] strs = date.split("/");
		if (strs.length != 2) {
			throw new InvalidCardException();
		}

		String expirationDate = null;
		int month = -1;
		int year = -1;

		try {
			month = Integer.parseInt(strs[0]);
			year = Integer.parseInt(strs[1]);
			if (month < 1 || month > 12 || year < Calendar.getInstance().get(Calendar.YEAR) % 100 || year > 100) {
				throw new InvalidCardException();
			}
			expirationDate = strs[0] + strs[1];

		} catch (Exception ex) {
			throw new InvalidCardException();
		}

		return expirationDate;
	}

	/**
	 * Pay order, and then return the result with a message.
	 *
	 * @param amount         - the amount to pay
	 * @param contents       - the transaction contents
	 * @param cardNumber     - the card number
	 * @param cardHolderName - the card holder name
	 * @param expirationDate - the expiration date in the format "mm/yy"
	 * @param securityCode   - the cvv/cvc code of the credit card
	 * @return {@link java.util.Map Map} represent the payment result with a
	 *         message.
	 *
     * Coupling
	 * The method payOrder accepts multiple parameters, including transaction data such as the payment amount and transaction contents.
	 * There is a level of data coupling through the direct passing of multiple pieces of data.
	 * This is an example of Data Coupling.
     *
     * Cohesion
     *Procedural Cohesion: This function focuses on some specific steps to make payments such as creating CreditCard, InterbankSubsystem and handling related exceptions.
     *Temporal Cohesion: CreditCard and InterbankSubsystem objects at the same time
	 */
	public Map<String, String> payOrder(int amount, String contents, String cardNumber, String cardHolderName,
			String expirationDate, String securityCode) {
		Map<String, String> result = new Hashtable<String, String>();
		result.put("RESULT", "PAYMENT FAILED!");
		try {
			this.card = new CreditCard(cardNumber, cardHolderName, Integer.parseInt(securityCode),
					getExpirationDate(expirationDate));

			this.interbank = new InterbankSubsystem();
			PaymentTransaction transaction = interbank.payOrder(card, amount, contents);

			result.put("RESULT", "PAYMENT SUCCESSFUL!");
			result.put("MESSAGE", "You have succesffully paid the order!");
		} catch (PaymentException | UnrecognizedException ex) {
			result.put("MESSAGE", ex.getMessage());
		}
		return result;
	}

	/**
     * Coupling
	 * This is an example of Data Coupling.
     *
     * Cohesion
     *Functional Cohesion: The components in the function perform the sole function of emptying the card
 	 */
	public void emptyCart(){
        Cart.getCart().emptyCart();
    }
}