import { executeCide } from "../Services/services";
import { useMutation } from "@tanstack/react-query";
import { IExecute } from "../Services/services";
import { useConsoleStore } from "../store";
export const useExecute = () => {
  const { consoleText, setConsoleText, setTimeExecution, setIsError } =
    useConsoleStore();
  return useMutation({
    mutationFn: (data: IExecute) => executeCide(data),
    onSuccess: (data) => {
      let newConsoleText = "";
      let isError = false;
      if (data.data.stdout !== "null") {
        newConsoleText = consoleText.trim() + "\n" + data.data.stdout.trim();
      } else if (data.data.stderr !== "null") {
        newConsoleText = consoleText.trim() + "\n" + data.data.stderr.trim();
        isError = true;
      }
      setConsoleText(newConsoleText);
      setTimeExecution(data.data.executionTime);
      setIsError(isError);
      console.log(data);
    },
  });
};
