import { useConsoleStore } from "../../store";
import "./Console.scss";
const Console = () => {
  const { consoleText, setConsoleText, timeExecution, isError } =
    useConsoleStore();
  return (
    <div className="console">
      <span>Console</span>
      <textarea
        className={isError ? "console__textarea-error" : "console__textarea"}
        value={consoleText}
        onChange={(event) => setConsoleText(event.target.value)}
      ></textarea>
      {timeExecution && <span>Время выполнения: {timeExecution} мс</span>}
    </div>
  );
};

export default Console;
