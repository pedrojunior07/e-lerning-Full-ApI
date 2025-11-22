package com.elearning.e_learning_core.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.CourseOrderDto;
import com.elearning.e_learning_core.Dtos.CreateOrderRequest;
import com.elearning.e_learning_core.Dtos.PaymentWalletDto;
import com.elearning.e_learning_core.Repository.CourseOrderRepository;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.Repository.InstructorRepository;
import com.elearning.e_learning_core.Repository.PaymentWalletRepository;
import com.elearning.e_learning_core.Repository.StudentRepository;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.CourseOrder;
import com.elearning.e_learning_core.model.OrderStatus;
import com.elearning.e_learning_core.model.PaymentWallet;
import com.elearning.e_learning_core.model.Student;
import com.elearning.e_learning_core.model.WalletType;

@Service
public class CourseOrderService {

    @Autowired
    private CourseOrderRepository orderRepository;

    @Autowired
    private PaymentWalletRepository walletRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    // ============ WALLET MANAGEMENT ============

    public ApiResponse<?> createWallet(Long instructorId, PaymentWalletDto dto) {
        PaymentWallet wallet = new PaymentWallet();
        wallet.setInstructor(instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instrutor não encontrado")));
        wallet.setWalletType(dto.walletType());
        wallet.setAccountName(dto.accountName());
        wallet.setAccountNumber(dto.accountNumber());
        wallet.setBankName(dto.bankName());
        wallet.setActive(true);

        wallet = walletRepository.save(wallet);
        return new ApiResponse<>("success", "Carteira criada com sucesso", 201, toWalletDto(wallet));
    }

    public ApiResponse<?> getInstructorWallets(Long instructorId) {
        List<PaymentWallet> wallets = walletRepository.findByInstructorId(instructorId);
        List<PaymentWalletDto> dtos = wallets.stream().map(this::toWalletDto).toList();
        return new ApiResponse<>("success", "Carteiras recuperadas", 200, dtos);
    }

    public ApiResponse<?> getActiveWalletsByType(Long instructorId, WalletType type) {
        List<PaymentWallet> wallets = walletRepository.findByInstructorIdAndWalletTypeAndActiveTrue(instructorId, type);
        List<PaymentWalletDto> dtos = wallets.stream().map(this::toWalletDto).toList();
        return new ApiResponse<>("success", "Carteiras recuperadas", 200, dtos);
    }

    public ApiResponse<?> updateWallet(Long walletId, PaymentWalletDto dto) {
        PaymentWallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));

        wallet.setWalletType(dto.walletType());
        wallet.setAccountName(dto.accountName());
        wallet.setAccountNumber(dto.accountNumber());
        wallet.setBankName(dto.bankName());
        wallet.setActive(dto.active());

        wallet = walletRepository.save(wallet);
        return new ApiResponse<>("success", "Carteira atualizada", 200, toWalletDto(wallet));
    }

    public ApiResponse<?> deleteWallet(Long walletId) {
        walletRepository.deleteById(walletId);
        return new ApiResponse<>("success", "Carteira removida", 200, null);
    }

    // ============ ORDER MANAGEMENT ============

    @Transactional
    public ApiResponse<?> createOrder(CreateOrderRequest request) {
        // Verificar se já existe pedido pendente ou aprovado
        boolean exists = orderRepository.findByStudentIdAndCourseIdAndStatusIn(
                request.studentId(),
                request.courseId(),
                Arrays.asList(OrderStatus.PENDING, OrderStatus.PROOF_UPLOADED, OrderStatus.APPROVED)
        ).isPresent();

        if (exists) {
            return new ApiResponse<>("error", "Já existe um pedido para este curso", 400, null);
        }

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));

        PaymentWallet wallet = walletRepository.findById(request.walletId())
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));

        CourseOrder order = new CourseOrder();
        order.setStudent(student);
        order.setCourse(course);
        order.setPaymentMethod(request.paymentMethod());
        order.setAmount(course.getPrice());
        order.setSelectedWallet(wallet);
        order.setStatus(OrderStatus.PENDING);

        order = orderRepository.save(order);
        return new ApiResponse<>("success", "Pedido criado com sucesso", 201, toOrderDto(order));
    }

    public ApiResponse<?> getStudentOrders(Long studentId) {
        List<CourseOrder> orders = orderRepository.findByStudentIdOrderByOrderDateDesc(studentId);
        List<CourseOrderDto> dtos = orders.stream().map(this::toOrderDto).toList();
        return new ApiResponse<>("success", "Pedidos recuperados", 200, dtos);
    }

    public ApiResponse<?> getInstructorOrders(Long instructorId) {
        List<CourseOrder> orders = orderRepository.findByInstructorId(instructorId);
        List<CourseOrderDto> dtos = orders.stream().map(this::toOrderDto).toList();
        return new ApiResponse<>("success", "Pedidos recuperados", 200, dtos);
    }

    public ApiResponse<?> getInstructorOrdersByStatus(Long instructorId, OrderStatus status) {
        List<CourseOrder> orders = orderRepository.findByInstructorIdAndStatus(instructorId, status);
        List<CourseOrderDto> dtos = orders.stream().map(this::toOrderDto).toList();
        return new ApiResponse<>("success", "Pedidos recuperados", 200, dtos);
    }

    @Transactional
    public ApiResponse<?> uploadProofOfPayment(Long orderId, String proofUrl) {
        CourseOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (order.getStatus() != OrderStatus.PENDING) {
            return new ApiResponse<>("error", "Pedido não está pendente", 400, null);
        }

        order.setProofOfPaymentUrl(proofUrl);
        order.setStatus(OrderStatus.PROOF_UPLOADED);

        order = orderRepository.save(order);
        return new ApiResponse<>("success", "Comprovativo enviado", 200, toOrderDto(order));
    }

    @Transactional
    public ApiResponse<?> approveOrder(Long orderId) {
        CourseOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (order.getStatus() != OrderStatus.PROOF_UPLOADED) {
            return new ApiResponse<>("error", "Pedido não tem comprovativo", 400, null);
        }

        order.setStatus(OrderStatus.APPROVED);
        order.setValidatedDate(LocalDateTime.now());

        // Inscrever estudante no curso
        Student student = order.getStudent();
        Course course = order.getCourse();
        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
            studentRepository.save(student);
        }

        order = orderRepository.save(order);
        return new ApiResponse<>("success", "Pedido aprovado e estudante inscrito no curso", 200, toOrderDto(order));
    }

    @Transactional
    public ApiResponse<?> rejectOrder(Long orderId, String reason) {
        CourseOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(OrderStatus.REJECTED);
        order.setRejectionReason(reason);
        order.setValidatedDate(LocalDateTime.now());

        order = orderRepository.save(order);
        return new ApiResponse<>("success", "Pedido rejeitado", 200, toOrderDto(order));
    }

    public ApiResponse<?> getOrderById(Long orderId) {
        CourseOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return new ApiResponse<>("success", "Pedido recuperado", 200, toOrderDto(order));
    }

    // ============ MAPPERS ============

    private PaymentWalletDto toWalletDto(PaymentWallet wallet) {
        return new PaymentWalletDto(
                wallet.getId(),
                wallet.getWalletType(),
                wallet.getAccountName(),
                wallet.getAccountNumber(),
                wallet.getBankName(),
                wallet.isActive()
        );
    }

    private CourseOrderDto toOrderDto(CourseOrder order) {
        return new CourseOrderDto(
                order.getId(),
                order.getStudent().getId(),
                order.getStudent().getFirstName() + " " + order.getStudent().getLastName(),
                order.getCourse().getId(),
                order.getCourse().getTitle(),
                order.getCourse().getThumbnailPath(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getAmount(),
                order.getProofOfPaymentUrl(),
                order.getOrderDate(),
                order.getValidatedDate(),
                order.getRejectionReason(),
                order.getSelectedWallet() != null ? toWalletDto(order.getSelectedWallet()) : null
        );
    }
}
