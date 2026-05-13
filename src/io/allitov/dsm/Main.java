import io.allitov.dsm.algorithm.DualSimplexSolver;
import io.allitov.dsm.model.Problem;
import io.allitov.dsm.util.ProblemReader;

void main() throws IOException {
    Problem problem = ProblemReader.readProblemFromFile("./resources/inf_solutions.txt");
    IO.println(problem);
    DualSimplexSolver solver = new DualSimplexSolver();
    solver.solve(problem);
}
