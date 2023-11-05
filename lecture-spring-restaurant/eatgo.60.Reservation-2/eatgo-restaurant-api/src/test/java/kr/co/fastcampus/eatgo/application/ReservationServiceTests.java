package kr.co.fastcampus.eatgo.application;

import kr.co.fastcampus.eatgo.domain.Reservation;
import kr.co.fastcampus.eatgo.domain.ReservationRepository;
import org.hibernate.validator.constraints.Range;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class ReservationServiceTests {

    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        reservationService = new ReservationService(reservationRepository);
    }

    @Test
    public void getReservation() {
        Long restaurantId = 1004L;
        List<Reservation> reservations =
                reservationService.getReservation(restaurantId);

        verify(reservationRepository).findAllByRestaurantId(restaurantId);
    }

}