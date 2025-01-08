/*
 * Copyright (C) 2022 Parisi Alessandro
 * This file is part of MaterialFX (https://github.com/palexdev/MaterialFX).
 *
 * MaterialFX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MaterialFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MaterialFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.jlipka.pickly.model;

import io.github.palexdev.materialfx.utils.RandomUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Getter
public class User {
	private UUID userID;
	private String username;
	@Setter
    private String name;
	@Setter
    private String surname;

	@Setter
	private byte[] avatar;

	@Setter
	private Status status;

	@Setter
	private String clientID;

	@Setter
	private int age;

	private LocalDateTime createdAt;

	public User() {
		this.userID = UUID.randomUUID();
		this.status = Status.OFFLINE;
		this.createdAt = LocalDateTime.now();
	}

	public User(String username) {
		this();
		this.username = username;
	}

	public User(String name, String surname) {
		this();
		this.name = name;
		this.surname = surname;
	}

	public static User ofSplit(String fullName, String split) {
		if (fullName == null || split == null) {
			throw new IllegalArgumentException("FullName and split cannot be null");
		}
		String[] nameArray = fullName.split(split);
		if (nameArray.length != 2) {
			throw new IllegalArgumentException(
					"FullName must contain exactly one occurrence of the split string"
			);
		}
		return new User(nameArray[0].trim(), nameArray[1].trim());
	}

	public User randomAge() {
		setAge(RandomUtils.random.nextInt(18, 81));
		return this;
	}

	public void setUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			throw new IllegalArgumentException("Username cannot be null or empty");
		}
		this.username = username;
	}

    public String getFullName() {
		return String.format("%s %s",
				name != null ? name : "",
				surname != null ? surname : ""
		).trim();
	}

	public boolean isOnline() {
		return status == Status.ONLINE;
	}

	public boolean hasAvatar() {
		return avatar != null && avatar.length > 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User user)) return false;
		return Objects.equals(userID, user.userID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userID);
	}

	@Override
	public String toString() {
		return String.format(
				"User{username='%s', name='%s', surname='%s', status=%s}",
				username,
				name,
				surname,
				status
		);
	}

	public static class Builder {
		private final User user;

		public Builder() {
			user = new User();
		}

		public Builder withUsername(String username) {
			user.setUsername(username);
			return this;
		}

		public Builder withName(String name) {
			user.setName(name);
			return this;
		}

		public Builder withSurname(String surname) {
			user.setSurname(surname);
			return this;
		}

		public Builder withStatus(Status status) {
			user.setStatus(status);
			return this;
		}

		public Builder withAge(int age) {
			user.setAge(age);
			return this;
		}

		public Builder withAvatar(byte[] avatar) {
			user.setAvatar(avatar);
			return this;
		}

		public Builder withClientId(String clientId) {
			user.setClientID(clientId);
			return this;
		}

		public User build() {
			return user;
		}
	}
}
