import { registration } from "../../Services/services";
import AuthForm from "../../Components/AuthForm/AuthForm";
const RegistrationPage = () => {
  return (
    <AuthForm
      mutationFn={registration}
      buttonText="Зарегистрироваться"
      title="Регистрация"
    />
  );
};
export default RegistrationPage;
