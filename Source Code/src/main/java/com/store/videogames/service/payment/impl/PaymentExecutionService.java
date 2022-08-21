package com.store.videogames.service.payment.impl;

import com.store.videogames.entites.Customer;
import com.store.videogames.entites.Videogame;
import com.store.videogames.entites.enums.PaymentMethod;
import com.store.videogames.exceptions.exception.EmailUnknownErrorException;
import com.store.videogames.service.customer.account.CustomerMoneyService;
import com.store.videogames.service.payment.IPaymentMethodTypeFactory;
import com.store.videogames.service.payment.IPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;


@Service
public class PaymentExecutionService
{
    private static final Logger logger = LoggerFactory.getLogger(PaymentExecutionService.class);

    private final CustomerMoneyService customerMoneyService;
    private final IPaymentMethodTypeFactory iPaymentMethodTypeFactory;

    @Autowired
    public PaymentExecutionService(IPaymentMethodTypeFactory paymentMethodTypeFactory, CustomerMoneyService customerMoneyService)
    {
        this.iPaymentMethodTypeFactory = paymentMethodTypeFactory;
		this.customerMoneyService = customerMoneyService;
    }

    public void buyGame(Customer customer, Videogame videogame, PaymentMethod paymentMethod)
    {
        customerMoneyService.isBalanceSufficentChecker(videogame.getPrice(), customer.getBalance());
        IPaymentService customerPaymentService = iPaymentMethodTypeFactory.getPaymentMethodService(paymentMethod);
        System.out.println(customerPaymentService);
        try
        {
            customerPaymentService.buyProduct(customer, videogame);
        }
        catch (MessagingException e)
        {
            throw new EmailUnknownErrorException("Error occured while sending an email to the customer");
        }
    }
}