package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/quizzes")
public class TaskController {
    @Autowired
    private QuizService quizService;

    public TaskController() {
    }

    @PostMapping
    public ResponseEntity<QuizEntity> add(@Valid @RequestBody final QuizEntity entity) {
        System.out.println("New quiz: " + entity);
        QuizEntity savedEntity = quizService.saveQuiz(entity);
        return new ResponseEntity<>(savedEntity, HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/solve")
    public AnswerFeedback getAnswer(@PathVariable Long id, @RequestBody Answer answer) {
        QuizEntity quiz = quizService.getQuizById(id).get();
        System.out.println(quiz);
        System.out.println("answer = " + answer);
        Set<Integer> gotAnswer = answer.getAnswer();
        Set<Integer> correctAnswer = quiz.getAnswer();
        System.out.println("gotAnswer = " + gotAnswer);
        System.out.println("correctAnswer = " + correctAnswer);
        if (gotAnswer.equals(correctAnswer)) {
            return new AnswerFeedback(true, "Congratulations, you're right!");
        }
        return new AnswerFeedback(false, "Wrong answer! Please, try again.");
    }

    @GetMapping
    public Collection<QuizEntity> getAllQuzzes() {
        System.out.println("Get all quizzes");
        return quizService.getAllQuizzes();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<QuizEntity> getQuiz(@PathVariable final Long id) {
        System.out.println("Get quiz with id: " + id);
        Optional<QuizEntity> quiz = quizService.getQuizById(id);
        if (quiz.isPresent()) {
            return new ResponseEntity<>(quiz.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }
}
