package com.findwork.findwork.Services;

import com.findwork.findwork.Entities.Users.UserCompany;
import com.findwork.findwork.Entities.Users.UserPerson;
import com.findwork.findwork.Repositories.UserCompanyRepository;
import com.findwork.findwork.Repositories.UserPersonRepository;
import com.findwork.findwork.Requests.EditCompanyRequest;
import com.findwork.findwork.Requests.EditPersonRequest;
import com.findwork.findwork.Requests.RegisterCompanyRequest;
import com.findwork.findwork.Requests.RegisterPersonRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest
{
    @Mock
    private UserPersonRepository userRepo;
    @Mock
    private UserCompanyRepository companyRepo;
    @InjectMocks
    private UserService userService;
    UserPerson user1 = new UserPerson("user1@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));
    UserCompany company1 = new UserCompany("comapny1@abv.bg", "password", "company", "description", "15", "1999", "address");
    @Test
    public void loadByUsernameTest()
    {
        Mockito.when(userRepo.findUserPersonByUsername("user1@abv.bg")).thenReturn(user1);
        Mockito.when(companyRepo.findUserCompanyByUsername("company1@abv.bg")).thenReturn(company1);
        assertEquals(userService.loadUserByUsername("user1@abv.bg"), user1); // finding an existing user works
        assertEquals(userService.loadUserByUsername("company1@abv.bg"), company1); // finding an existing company works
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("noUser@abv.bg")); // loading a nonexistent user/company throws an exception
    }
    @Test
    public void registerPersonTest() throws Exception {
        RegisterPersonRequest person = new RegisterPersonRequest("user1@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));
        UserPerson savedPerson = userService.registerPerson(person);
        assertEquals(person.getEmail(), savedPerson.getUsername()); //Saving a person with a new unique email works
        Mockito.when(userRepo.findUserPersonByUsername("user1@abv.bg")).thenReturn(user1);
        assertThrows(Exception.class, () -> userService.registerPerson(person)); //Saving a person with an already used email throws exception
    }
    @Test
    public void registerCompanyTest() throws Exception {
        RegisterCompanyRequest company = new RegisterCompanyRequest("company1","company1@abv.bg","password", "15","1999","address","description");
        UserCompany savedCompany = userService.registerCompany(company);
        assertEquals(company.getEmail(), savedCompany.getUsername()); //Saving a company with a new unique email works
        Mockito.when(companyRepo.findUserCompanyByUsername("company1@abv.bg")).thenReturn(company1);
        assertThrows(Exception.class, () -> userService.registerCompany(company)); //Saving a company with an already used email throws exception
    }

    @Test
    public void editPersonTest() throws Exception {
        EditPersonRequest request = new EditPersonRequest("ModifiedUser1@abv.bg", "password1", "firstName1", "lastName1", LocalDate.of(1999, 1, 2), "education1", "skills1");
        Mockito.when(userRepo.findUserPersonById(UUID.fromString("0000-00-00-00-000000"))).thenReturn(user1);
        userService.editPerson(UUID.fromString("0000-00-00-00-000000"), request);
        assertEquals(user1.getUsername(), request.getEmail());
        assertEquals("password1", request.getPassword()); // hardcoded because otherwise SpringSecurity blocks user1.getPassword()
        assertEquals(user1.getFirstName(), request.getFirstName());
        assertEquals(user1.getLastName(), request.getLastName());
        assertEquals(user1.getBirthDate(), request.getBirthDate());
        Mockito.when(userRepo.findUserPersonByUsername("ModifiedUser1@abv.bg")).thenReturn(user1); // Fake scenario with email in use
        assertThrows(Exception.class, () -> userService.editPerson(UUID.fromString("0000-00-00-00-000000"), request)); //Saving a company with an already used email throws exception
    }
    @Test
    public void editCompanyTest() throws Exception
    {
        EditCompanyRequest request = new EditCompanyRequest("ModifiedCompany1@abv.bg", "password1", "name1","description1","16", "2000", "address1");
        Mockito.when(companyRepo.findUserCompanyById(UUID.fromString("0000-00-00-00-000000"))).thenReturn(company1);
        userService.editCompany(UUID.fromString("0000-00-00-00-000000"), request);
        assertEquals(company1.getUsername(), request.getEmail());
        assertEquals("password1", request.getPassword()); // hardcoded because otherwise SpringSecurity blocks company1.getPassword()
        assertEquals(company1.getName(), request.getName());
        assertEquals(company1.getDescription(), request.getDescription());
        assertEquals(company1.getEmployeeCount(), request.getEmployeeCount());
        assertEquals(company1.getFoundingYear(), request.getFoundingYear());
        assertEquals(company1.getAddress(), request.getAddress());
        Mockito.when(companyRepo.findUserCompanyByUsername("ModifiedCompany1@abv.bg")).thenReturn(company1); // Fake scenario with email in use
        assertThrows(Exception.class, () -> userService.editCompany(UUID.fromString("0000-00-00-00-000000"), request)); //Saving a company with an already used
    }

    @Test
    public void adminDeleteAccountTest()
    {
        //Given
        Mockito.when(userRepo.findUserPersonByUsername("user1@abv.bg")).thenReturn(user1);
        //When
        userService.adminDeleteAccount("user1@abv.bg");
        //Then
        verify(userRepo, Mockito.times(1)).delete(ArgumentMatchers.any(UserPerson.class));

        assertThrows(Exception.class, () -> userService.adminDeleteAccount("noUser@abv.bg"));
    }
}
