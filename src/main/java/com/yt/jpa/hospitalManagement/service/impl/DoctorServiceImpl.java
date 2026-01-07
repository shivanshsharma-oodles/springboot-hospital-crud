package com.yt.jpa.hospitalManagement.service.impl;

import com.yt.jpa.hospitalManagement.dto.request.AdminCreateDoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorSlotRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.patch.DoctorPatchRequestDto;
import com.yt.jpa.hospitalManagement.dto.request.DoctorRequestDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.DoctorSlotResponseDto;
import com.yt.jpa.hospitalManagement.dto.response.publicDto.DoctorPublicDto;
import com.yt.jpa.hospitalManagement.entity.*;
import com.yt.jpa.hospitalManagement.enums.AppointmentStatus;
import com.yt.jpa.hospitalManagement.enums.DoctorStatus;
import com.yt.jpa.hospitalManagement.enums.Role;
import com.yt.jpa.hospitalManagement.exception.DuplicateResourceException;
import com.yt.jpa.hospitalManagement.exception.ResourceNotFoundException;
import com.yt.jpa.hospitalManagement.exception.UnauthorizedActionException;
import com.yt.jpa.hospitalManagement.mapper.DoctorMapper;
import com.yt.jpa.hospitalManagement.repository.*;
import com.yt.jpa.hospitalManagement.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;
    private final DoctorSlotRepository doctorSlotRepository;
    private final AppointmentRepository appointmentRepository;

    /* Get All Doctors */
    @Override
    public List<DoctorPublicDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findByStatusNot(DoctorStatus.ARCHIVED);

//        Convert Doctor to Doctor Response Dto
        return doctors.stream()
                .map(d -> modelMapper.map(d, DoctorPublicDto.class))
                .toList();
    }

    @Override
    public DoctorPublicDto getDoctorPubliclyById(Long id){
        Doctor doctor = doctorRepository.findByIdAndStatusNot(id, DoctorStatus.ARCHIVED)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

        return modelMapper.map(doctor, DoctorPublicDto.class);
    }

    @Override
    public List<DoctorPublicDto> getDoctorsByDepartmentId(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department does not exist."));

        List<Doctor> doctors = doctorRepository.findByDepartmentAndStatusNot(department, DoctorStatus.ARCHIVED);

        return doctors.stream()
                .map(d -> modelMapper.map(d, DoctorPublicDto.class))
                .toList();
    }

    /* Get Doctor(Own) By id */
    @Override
    public DoctorResponseDto getDoctorsById(Long id) {
        Doctor doctor = doctorRepository.findByIdAndStatusNot(id, DoctorStatus.ARCHIVED)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

//        DoctorResponseDto doctorResponseDto = modelMapper.map(doctor, DoctorResponseDto.class);
//        doctorResponseDto.setEmail(doctor.getUser().getEmail());
//        return doctorResponseDto;
        return doctorMapper.toResponseDto(doctor);
    }

    /* Create Doctor */
    @Override
    public DoctorResponseDto createDoctor(String email, DoctorRequestDto doctorRequestDto) {
        if (doctorRepository.existsByUser_EmailAndStatusNot(
                email, DoctorStatus.ARCHIVED
        )) {
            throw new DuplicateResourceException("Doctor already exists with same email");
        }
        if (doctorRepository.existsByPhoneAndStatusNot(
                doctorRequestDto.getPhone(), DoctorStatus.ARCHIVED
        )) {
            throw new DuplicateResourceException("Doctor already exists with same phone");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        // Load Dept from DB
        Department department = departmentRepository.findById(doctorRequestDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department does not exist"));

        Doctor doctor = doctorMapper.toEntity(doctorRequestDto);
        doctor.setUser(user);
        doctor.setDepartment(department);

        doctorRepository.save(doctor);
        return doctorMapper.toResponseDto(doctor);
    }

    /* Update Doctor */
    @Override
    public DoctorResponseDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Doctor with this id exists"));


        if (doctorRepository.existsByPhoneAndStatusNotAndIdNot(doctorRequestDto.getPhone(), DoctorStatus.ARCHIVED, id)) {
            throw new DuplicateResourceException("Doctor already exists with same phone");
        }


        Department department = departmentRepository.findById(doctorRequestDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department does not exist"));

        doctorMapper.updateEntity(doctorRequestDto, doctor);
        doctor.setDepartment(department);

//        return modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
        doctorRepository.save(doctor);
        return doctorMapper.toResponseDto(doctor);
    }


    /* Update Partial Doctor */
    @Override
    public DoctorResponseDto updatePartialDoctor(Long id, DoctorPatchRequestDto doctorPatchRequestDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

        if (doctorPatchRequestDto.getName() != null && !doctorPatchRequestDto.getName().isEmpty()) {
            doctor.setName(doctorPatchRequestDto.getName());
        }
        if (doctorPatchRequestDto.getPhone() != null && !doctorPatchRequestDto.getPhone().isEmpty()) {
            if (doctorRepository.existsByPhoneAndStatusNot(doctorPatchRequestDto.getPhone(), DoctorStatus.ARCHIVED)) {
                throw new DuplicateResourceException("Doctor already exists with same phone");
            }
            doctor.setPhone(doctorPatchRequestDto.getPhone());
        }
        if (doctorPatchRequestDto.getDob() != null) {
            doctor.setDob(doctorPatchRequestDto.getDob());
        }
        if (doctorPatchRequestDto.getGender() != null) {
            doctor.setGender(doctorPatchRequestDto.getGender());
        }
        if (doctorPatchRequestDto.getAddress() != null) {
            doctor.setAddress(doctorPatchRequestDto.getAddress());
        }
        if (doctorPatchRequestDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(doctorPatchRequestDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department does not exist"));
            doctor.setDepartment(department);
        }

//        return modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
        doctorRepository.save(doctor);
        return doctorMapper.toResponseDto(doctor);
    }

    /* Delete Doctor */
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findByIdAndStatusNot(id, DoctorStatus.ARCHIVED)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

        doctor.setStatus(DoctorStatus.ARCHIVED);
        doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public DoctorResponseDto createDoctorByAdmin(AdminCreateDoctorRequestDto dto) {

        // 1 Validate uniqueness
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
        }

        if (doctorRepository.existsByPhoneAndStatusNot(
                dto.getPhone(), DoctorStatus.ARCHIVED)) {
            throw new DuplicateResourceException("Doctor already exists with same phone");
        }

        // 2️Create USER (AUTH)
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(Set.of(Role.DOCTOR))
                .build();

        userRepository.save(user);

        // 3 Load Department
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        // Create DOCTOR (DOMAIN)
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setPhone(dto.getPhone());
        doctor.setDob(dto.getDob());
        doctor.setGender(dto.getGender());
        doctor.setAddress(dto.getAddress());
        doctor.setDepartment(department);
        doctor.setUser(user);

        doctorRepository.save(doctor);

        return doctorMapper.toResponseDto(doctor);
    }

    @Override
    public void createDoctorSlot(Long id, DoctorSlotRequestDto dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor does not exist"));

//        Illegal Slot
        if (dto.getStartTime().isAfter(dto.getEndTime())
                || dto.getStartTime().equals(dto.getEndTime())) {
            throw new IllegalArgumentException("Invalid slot timing");
        }

        // Prevent overlapping slots
        boolean overlap = doctorSlotRepository
                .existsByDoctorAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        doctor,
                        dto.getDate(),
                        dto.getEndTime(),
                        dto.getStartTime()
                );

        if (overlap) {
            throw new IllegalStateException("Slot overlaps with existing slot");
        }
        DoctorSlot slot = new DoctorSlot();
        slot.setDoctor(doctor);
        slot.setDate(dto.getDate());
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());

        doctorSlotRepository.save(slot);
    }

    @Override
    public List<DoctorSlotResponseDto> getSlotsByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        LocalDate today = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

        // Get booked / reserved / completed slot IDs
        List<Long> reservedSlotIds = appointmentRepository
                .findDoctorSlotIdsByDoctorAndAppointmentStatusIn(
                        doctor,
                        List.of(
                                AppointmentStatus.SCHEDULED,
                                AppointmentStatus.PENDING,
                                AppointmentStatus.COMPLETED
                        )
                );

        // Fetch only FREE slots
        List<DoctorSlot> slots = reservedSlotIds.isEmpty()
                ? doctorSlotRepository.findByDoctorAndDateGreaterThanEqual(
                doctor,
                LocalDate.now()
        )
                : doctorSlotRepository.findByDoctorAndDateGreaterThanEqualAndIdNotIn(
                doctor,
                LocalDate.now(),
                reservedSlotIds
        );

        return slots.stream()
                .filter(slot -> {
//                        future date -> allow
                    if (slot.getDate().isAfter(today)) return true;

                    // same date → only show if slot starts after 15min from now
                    if (slot.getDate().isEqual(today)) {
                        return slot.getStartTime().isAfter(timeNow.plusMinutes(15));
                    }
                    return false;
                })
                .map(slot -> modelMapper.map(slot, DoctorSlotResponseDto.class))
                .toList();
    }

    @Transactional
    @Override
    public void deleteDoctorSlot(Long doctorId, Long slotId) {

        DoctorSlot slot = doctorSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot does not exist"));

        if(!slot.getDoctor().getId().equals(doctorId)) {
            throw  new UnauthorizedActionException("Can not delete another doctor's slot");
        }

//        Prevent Delete on booked slot
        boolean alreadyBooked = appointmentRepository
                .existsByDoctorSlotAndAppointmentStatusIn(
                        slot,
                        List.of(AppointmentStatus.PENDING, AppointmentStatus.SCHEDULED, AppointmentStatus.COMPLETED)
                );

        if (alreadyBooked) {
            throw new IllegalStateException("Slot already booked, and can not be deleted");
        }

        doctorSlotRepository.delete(slot);
    }

}