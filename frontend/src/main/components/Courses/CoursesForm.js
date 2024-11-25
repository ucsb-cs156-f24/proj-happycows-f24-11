import { Button, Form, Row, Col } from 'react-bootstrap';
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'

function CoursesForm({ initialContents, submitAction, buttonLabel = "Create" }) {
    // Stryker disable all
    const {
        register,
        formState: { errors },
        handleSubmit,
    } = useForm(
        { defaultValues: initialContents || {}, }
    );

    const navigate = useNavigate();
    
    // Stryker restore all
    return (

        <Form onSubmit={handleSubmit(submitAction)}>


            <Row>

                {initialContents && (
                    <Col>
                        <Form.Group className="mb-3" >
                            <Form.Label htmlFor="id">Id</Form.Label>
                            <Form.Control
                                data-testid="CoursesForm-id"
                                id="id"
                                type="text"
                                {...register("id")}
                                value={initialContents.id}
                                disabled
                            />
                        </Form.Group>
                    </Col>
                )}

                <Col>
                    <Form.Group className="mb-3" >
                        <Form.Label htmlFor="name">Name</Form.Label>
                        <Form.Control
                            data-testid="CoursesForm-name"
                            id="name"
                            type="text"
                            isInvalid={Boolean(errors.name)}
                            {...register("name", { required: true })}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.name && 'Name is required. '}
                        </Form.Control.Feedback>
                    </Form.Group>
                </Col>
            </Row>

            <Row>
                <Col>
                    <Form.Group className="mb-3" >
                        <Form.Label htmlFor="term">Term</Form.Label>
                        <Form.Control
                            data-testid="CoursesForm-term"
                            id="term"
                            type="text"
                            isInvalid={Boolean(errors.term)}
                            {...register("term", { required: true })}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.term && 'Term is required. '}
                        </Form.Control.Feedback>
                    </Form.Group>
                </Col>
            </Row>

            <Row>
                <Col>
                    <Button
                        type="submit"
                        data-testid="CoursesForm-submit"
                    >
                        {buttonLabel}
                    </Button>
                    <Button
                        variant="Secondary"
                        onClick={() => navigate(-1)}
                        data-testid="CoursesForm-cancel"
                    >
                        Cancel
                    </Button>
                </Col>
            </Row>
        </Form>

    )
}

export default CoursesForm