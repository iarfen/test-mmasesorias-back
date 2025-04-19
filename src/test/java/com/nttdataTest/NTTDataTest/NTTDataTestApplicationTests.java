package com.nttdataTest.NTTDataTest;

import com.nttdataTest.dao.UsersDAO;
import com.nttdataTest.dto.PhoneDTO;
import com.nttdataTest.dto.UserDTO;
import com.nttdataTest.dto.UserUpdateDTO;
import com.nttdataTest.model.Phone;
import com.nttdataTest.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class NTTDataTestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UsersDAO usersDAO;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .build();
	}

	@Test
	public void givenUserObject_whenCreateUser_thenReturnSavedUser() throws Exception{
		PhoneDTO phoneDTO1 = PhoneDTO.builder().numero(11234L).codigoCiudad(9L).codigoPais(56L).build();
		PhoneDTO phoneDTO2 = PhoneDTO.builder().numero(43556L).codigoCiudad(9L).codigoPais(56L).build();
		List<PhoneDTO> phonesDTO = new ArrayList<>();
		phonesDTO.add(phoneDTO1);
		phonesDTO.add(phoneDTO2);
		Phone phone1 = Phone.builder().number(11234L).cityCode(9L).countryCode(56L).build();
		Phone phone2 = Phone.builder().number(43556L).cityCode(9L).countryCode(56L).build();
		List<Phone> phones = new ArrayList<>();
		phones.add(phone1);
		phones.add(phone2);
		UserDTO userDTO = UserDTO.builder()
				.nombre("User 1")
				.correo("email1@outlook.com")
				.contrasena("test22")
				.telefonos(phonesDTO)
				.build();
		User user = User.builder()
                .id(1L)
                .name("User 1")
				.email("email1@outlook.com")
				.password("test22")
				.phones(phones)
				.build();
		when(usersDAO.save(user)).thenReturn(user);

		ResultActions response = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDTO)));

		response.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void givenListOfUsers_whenGetAllUsers_thenReturnUsersList() throws Exception{
		List<User> listUsers = new ArrayList<>();
		listUsers.add(User.builder().id(1L).name("This is user 1").build());
		listUsers.add(User.builder().id(2L).name("This is user 2").build());
		when(usersDAO.findAll()).thenReturn(listUsers);

		ResultActions response = mockMvc.perform(get("/users"));

		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listUsers.size())));
	}

	@Test
	public void givenUserId_whenGetUserById_thenReturnUserObject() throws Exception{
		Long userId = 1L;
		User user = User.builder()
				.id(userId)
				.name("Name of user 1")
				.build();
		when(usersDAO.findById(userId)).thenReturn(Optional.of(user));

		ResultActions response = mockMvc.perform(get("/users/{userId}", userId));

		response.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void givenInvalidUserId_whenGetUserById_thenReturnEmpty() throws Exception{
		Long userId = 1L;
		given(usersDAO.findById(userId)).willReturn(Optional.empty());

		ResultActions response = mockMvc.perform(get("/users/{userId}", userId));

		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void givenUpdatedUser_whenUpdateUser_thenReturnUpdateUserObject() throws Exception{
		Long userId = 1L;
		User user = User.builder()
				.id(userId)
				.name("Name of user 1")
				.token("qwerewr")
				.build();

		PhoneDTO phoneDTO1 = PhoneDTO.builder().numero(11234L).codigoCiudad(9L).codigoPais(56L).build();
		PhoneDTO phoneDTO2 = PhoneDTO.builder().numero(43556L).codigoCiudad(9L).codigoPais(56L).build();
		List<PhoneDTO> phonesDTO = new ArrayList<>();
		phonesDTO.add(phoneDTO1);
		phonesDTO.add(phoneDTO2);
		UserUpdateDTO updatedUser = UserUpdateDTO.builder()
				.nombre("Name of user 1")
				.correo("user123@outlook.com")
				.contrasena("try4556")
				.telefonos(phonesDTO)
				.token("qwerewr")
				.build();
		given(usersDAO.findById(userId)).willReturn(Optional.of(user));
		given(usersDAO.save(any(User.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));

		ResultActions response = mockMvc.perform(put("/users/{userId}", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedUser)));

		response.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void givenUpdatedUser_whenPatchUser_thenReturnUpdateUserObject() throws Exception{
		Long userId = 1L;
		User user = User.builder()
				.id(userId)
				.name("Name of user 1")
				.token("qwerewr")
				.build();

		PhoneDTO phoneDTO1 = PhoneDTO.builder().numero(11234L).codigoCiudad(9L).codigoPais(56L).build();
		PhoneDTO phoneDTO2 = PhoneDTO.builder().numero(43556L).codigoCiudad(9L).codigoPais(56L).build();
		List<PhoneDTO> phonesDTO = new ArrayList<>();
		phonesDTO.add(phoneDTO1);
		phonesDTO.add(phoneDTO2);
		UserUpdateDTO updatedUser = UserUpdateDTO.builder()
				.nombre("Name of user 1")
				.correo("user123@outlook.com")
				.contrasena("try4556")
				.telefonos(phonesDTO)
				.token("qwerewr")
				.build();
		given(usersDAO.findById(userId)).willReturn(Optional.of(user));
		given(usersDAO.save(any(User.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));

		ResultActions response = mockMvc.perform(patch("/users/{userId}", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedUser)));

		response.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void givenUpdatedUser_whenUpdateUser_thenReturn404() throws Exception{
		Long userId = 1L;
		User user = User.builder()
				.id(userId)
				.name("Name of user 1")
				.build();
		given(usersDAO.findById(userId)).willReturn(Optional.empty());

		ResultActions response = mockMvc.perform(put("/users/{userId}", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)));

		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void givenUserId_whenDeleteUser_thenReturn200() throws Exception{
		Long userId = 1L;
		User user = User.builder()
				.id(userId)
				.name("Name of user 1")
				.build();
		when(usersDAO.findById(userId)).thenReturn(Optional.of(user));
		willDoNothing().given(usersDAO).deleteById(userId);

		ResultActions response = mockMvc.perform(delete("/users/{userId}", userId));

		response.andExpect(status().isOk())
				.andDo(print());
	}
}