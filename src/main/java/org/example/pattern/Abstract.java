package org.example.pattern;



interface Button {
    void render();
}

interface Checkbox {
    void render();
}


class WindowsButton implements Button {
    @Override
    public void render() {
        System.out.println("Render Windows button");
    }
}

class MacOSButton implements Button {
    @Override
    public void render() {
        System.out.println("Render macOS button");
    }
}

class WindowsCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("Render Windows checkbox");
    }
}

class MacOSCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("Render macOS checkbox");
    }
}


interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}


class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}

class MacOSFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new MacOSButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacOSCheckbox();
    }
}


class Application {
    private Button button;
    private Checkbox checkbox;

    public Application(GUIFactory factory) {
        button = factory.createButton();
        checkbox = factory.createCheckbox();
    }

    public void render() {
        button.render();
        checkbox.render();
    }
}