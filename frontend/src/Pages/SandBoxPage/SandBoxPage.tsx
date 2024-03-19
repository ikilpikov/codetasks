import Console from "../../Components/ConsoleComponent/Console";
import ConditionPanel from "../../Components/ConditionPanel/ConditionPanel";
import CodeBlock from "../../Components/CodeBlock/CodeBlock";
import SolutionInfo from "../../Components/SolutionModal/SolutionModal";
import "./SandBoxPage.scss";
import { useAttempt } from "../../Hooks/useAttempt";
import { useExecute } from "../../Hooks/useExecute";
import {
  useConsoleStore,
  useCodeTextStore,
  useSandBoxStore,
  useSolutionStore,
} from "../../store";
import { useParams } from "react-router-dom";

const SandBoxPage = () => {
  const { taskId } = useParams();
  const { mutate: attemptMutate } = useAttempt();
  const { mutate: executeMutate } = useExecute();
  const { selectedLanguage } = useSandBoxStore();
  const { consoleText } = useConsoleStore();
  const { text: code } = useCodeTextStore();
  const { visible } = useSolutionStore();

  const attempt = () => {
    const data = {
      taskId: taskId,
      language: selectedLanguage,
      code: code,
    };
    console.log(data);

    attemptMutate(data);
  };

  const execute = () => {
    const data = {
      language: selectedLanguage,
      stdin: consoleText,
      code: code,
    };
    console.log(data);

    executeMutate(data);
  };
  return (
    <div className="sandBox">
      <div className="sandBox__main">
        <ConditionPanel />
        <CodeBlock />
      </div>
      {visible && <SolutionInfo taskId={taskId} />}
      <Console />
      <button onClick={execute}>Execute code</button>
      <button onClick={attempt}>Send code</button>
    </div>
  );
};

export default SandBoxPage;
