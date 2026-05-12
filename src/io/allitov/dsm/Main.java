import io.allitov.dsm.model.Problem;
import io.allitov.dsm.util.ProblemReader;

void main() throws IOException {
    Problem problem = ProblemReader.readProblemFromFile("./resources/lp_problem.txt");
    IO.println(problem);
}
