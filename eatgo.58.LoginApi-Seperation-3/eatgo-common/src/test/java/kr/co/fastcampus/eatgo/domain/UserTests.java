package kr.co.fastcampus.eatgo.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserTests {

    @Test
    public void create() {
        User user = User.builder()
                .email("test@example.com")
                .name("Test")
                .level(100L)
                .build();

        assertThat(user.getName(), is("Test"));
        assertThat(user.isAdmin(), is(true));

        user.deactivate();

        assertThat(user.isActive(), is(false));
    }

    @Test
    public void restaurantOwner() {
        User user = User.builder().level(1L).build();

        assertThat(user.isRestaurantOwner(), is(false));

        user.setRestaurantId(1004L);

        assertThat(user.isRestaurantOwner(), is(true));
        assertThat(user.getRestaurantId(), is(1004L));
    }
}