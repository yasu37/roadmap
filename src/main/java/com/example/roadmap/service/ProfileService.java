package com.example.roadmap.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.roadmap.dto.ProfileForm;
import com.example.roadmap.entity.Profile;
import com.example.roadmap.repository.ProfileRepository;

@Service
public class ProfileService {

	private static final String[] ALLOWED_AGE_RANGES = { "18-20", "21-23", "24-26", "27+" };
	private static final String[] ALLOWED_OCCUPATIONS = { "student", "employee", "part-time", "other" };

	private final ProfileRepository profileRepository;

	public ProfileService(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	@Transactional(readOnly = true)
	public ProfileForm getProfileForm(UUID userId) {
		return profileRepository.findByUserId(userId)
				.map(this::toForm)
				.orElseGet(ProfileForm::new);
	}

	@Transactional
	public void saveProfile(UUID userId, ProfileForm form) {
		validate(form);

		OffsetDateTime now = OffsetDateTime.now();

		Profile profile = profileRepository.findByUserId(userId).orElseGet(() -> {
			Profile newProfile = new Profile();
			newProfile.setUserId(userId);
			newProfile.setCreatedAt(now);
			return newProfile;
		});

		profile.setNickname(trimToNull(form.getNickname()));
		profile.setAgeRange(trimToNull(form.getAgeRange()));
		profile.setOccupation(trimToNull(form.getOccupation()));
		profile.setUpdatedAt(now);

		profileRepository.save(profile);
	}

	private ProfileForm toForm(Profile profile) {
		ProfileForm form = new ProfileForm();
		form.setNickname(profile.getNickname());
		form.setAgeRange(profile.getAgeRange());
		form.setOccupation(profile.getOccupation());
		return form;
	}

	private void validate(ProfileForm form) {
		String nickname = trimToNull(form.getNickname());
		String ageRange = trimToNull(form.getAgeRange());
		String occupation = trimToNull(form.getOccupation());

		if (nickname != null && nickname.length() > 100) {
			throw new IllegalArgumentException("ニックネームは100文字以内で入力してください。");
		}
		if (ageRange != null && !isAllowed(ageRange, ALLOWED_AGE_RANGES)) {
			throw new IllegalArgumentException("年齢帯の値が不正です。");
		}
		if (occupation != null && !isAllowed(occupation, ALLOWED_OCCUPATIONS)) {
			throw new IllegalArgumentException("属性の値が不正です。");
		}
	}

	private boolean isAllowed(String value, String[] allowed) {
		for (String item : allowed) {
			if (item.equals(value)) {
				return true;
			}
		}
		return false;
	}

	private String trimToNull(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}
}